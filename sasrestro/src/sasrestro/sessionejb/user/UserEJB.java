package sasrestro.sessionejb.user;
 
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import sasrestro.misc.SaltedPBKDFHash;
import sasrestro.model.user.User;
import sasrestro.sessionejb.core.GenericDAO;
import sasrestro.sessionejb.util.FiscalYrEJB;

/**
 * @author nebula
 *
 */

@Stateless
@LocalBean
public class UserEJB extends GenericDAO<User> {
 
	@EJB
	FiscalYrEJB fiscalYrEJB;
    public UserEJB() {
        super(User.class);
        System.out.println("UserDAO EJB created!");
    }
    @PostConstruct
    private void startBeanLife()
    {
    	System.out.println("UserDAO EJBean is Ready for Use!");
    	}
 
    public User findUserByEmail(String email){
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("email", email);     
 
        return super.findOneResult(User.FIND_BY_EMAIL, parameters);
    }
    
    public User isValidLogin(String email, String password) throws NoSuchAlgorithmException, InvalidKeySpecException{
        User user = findUserByEmail(email);
        //user.setFyModel(fiscalYrEJB.getActiveFiscalYear());
 
        if (user == null || !SaltedPBKDFHash.validatePassword(password, user.getPassword())) {
        	return null;
        }
        return user;
    }
    public List<User> listAll() {
        List<User> result = findAll();
        return result;
    }
    public void createUser(User user) throws NoSuchAlgorithmException, InvalidKeySpecException {
    	user.setPassword(SaltedPBKDFHash.createHash(user.getPassword()));
        save(user);
    }
 
    public void updateUser(User user) throws NoSuchAlgorithmException, InvalidKeySpecException {
        User persistedUser = find(user.getId());
        persistedUser.setEmail(user.getEmail());
        persistedUser.setName(user.getName());
        //persistedUser.setPassword(user.getPassword());
        persistedUser.setPassword(SaltedPBKDFHash.createHash(user.getPassword()));
        persistedUser.setRole(user.getRole());
        
        update(persistedUser);
    }
 
   /* public void deleteUser(User user) {
    	userDAO.beginTransaction();
        User persistedUser = userDAO.findReferenceOnly(user.getId());
        userDAO.delete(persistedUser);
        userDAO.commitAndCloseTransaction();
    }*/
    public User findUser(int userId) {
        User user = find(userId);
        return user;
    }
}
