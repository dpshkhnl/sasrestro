package sasrestro.misc;

import java.util.HashMap;
import java.util.Map;

public class JrUtils {

	/**
	 * @param args
	 */

	//	    System.out.println(format1.format(new BigDecimal(txtCurrency.getText())));
	/**
	 * @param bigNumber
	 * @return string equivalent to its Hindu-Arabic number system's place value.
	 * 
	 * Here, in this method, the @param bigNumber is first passed to another mehtod
	 * called toHinduArabicFormat(string @param) to get it processed into Hindu-Arabic
	 * number format and then further processed to spell its digit blocks according
	 * to Hindu-Arabic number place-value system.
	 */
	@SuppressWarnings("unused")
	public static String toWordsMiracle(String bigNumber)
	{
		System.out.println("zz.. unformatted input number is ="+bigNumber);
		String amtInWords="";
		/* The following commented block was first used as a part of this method.
		 * But, since we observed draw-backs of using both com.ibm.icu.text.DecimalFormat
		 * and java.text.DecimalFormat. So, the equivalent method had to be carried out
		 * by our self-excited method(below) called : toHinduArabicFormat(bigNumber).
		 * */

		/*if(bigNumber.contains(","))
		{
			bigNumber=bigNumber.replaceAll(",", "");
		}
		DecimalFormat df = new DecimalFormat("##,##0.00");
		com.ibm.icu.text.DecimalFormat format1 = (com.ibm.icu.text.DecimalFormat) NumberFormat.getCurrencyInstance(new Locale("en", "in"));
		System.out.println("the Number in international format is ="+df.format(Double.valueOf(bigNumber)));
		System.out.println("the Number in Hindu-Arabic format is ="+format1.format(Double.valueOf(bigNumber)));
		String src=format1.format(Double.valueOf(bigNumber)).substring(3);
		System.out.println("the Number After removing Rs. is ="+src);*/
		String src=toHinduArabicFormat(bigNumber);
		String[] srcAll=new String[2];
		if(src.contains("."))
		{
			srcAll=src.split("[.]");
		}
		else
		{
			srcAll[0]=src;
			srcAll[1]="0";// if no paisa was found set it to zero
		}


		System.out.println("After spliting  real part ="+srcAll[0]);
		System.out.println("After spliting imaginary part ="+srcAll[1]);
		/* The following map is used to map the hindu-Arabic number-system's 
		 * place-values according to thier position in the whole number.
		 * 
		 * We've optimized it to handle the greatest number possible to spell in
		 * Hindu-Arabic number system.
		 * 
		 * */
		Map<String, Object> placeValue = new HashMap<String, Object>();
		placeValue.put("1","");
		placeValue.put("2","Thousand");
		placeValue.put("3","Lakh");
		placeValue.put("4","Crore");
		placeValue.put("5","Arab");
		placeValue.put("6","Kharab");
		placeValue.put("7","Neel");
		placeValue.put("8","Padma");
		placeValue.put("9","Shankha");
		placeValue.put("10","Ank");
		placeValue.put("11","Jald");
		placeValue.put("12","Madh");
		placeValue.put("13","Parardha");
		placeValue.put("14","Ant");
		placeValue.put("15","Maha-Ant");
		placeValue.put("16","Shisht");
		placeValue.put("17","Singhar");
		placeValue.put("18","Maha-Singhar");//for input 10^37

		//split the number first
		String[] partsOfReal=srcAll[0].split(",");
		for(int i=0;i<partsOfReal.length;i++)
		{
			//System.out.println("After spliting Real parts at each occurance of a comma, "+i+"th part at "+(partsOfReal.length-i)+" has place value "+String.valueOf(placeValue.get(String.valueOf((partsOfReal.length-i))))+" place value from L 2 R is ="+partsOfReal[i]);


			int len, q=0, r=0;
			String ltr = " ";
			String Str = "";
			CNTL cntl= new CNTL();
			int num = Integer.parseInt(partsOfReal[i].equals("")?"0":partsOfReal[i]);


			if (num <= 0) System.out.println("Zero or Negative number not for conversion");

			while (num>0)
			{

				len = cntl.numberCount(num);

				//Take the length of the number and do letter conversion

				switch (len)

				{

				case 3:
					if (len == 3)
						r = num;
					ltr = cntl.threenum(r);
					Str = Str + ltr;
					num = 0;
					amtInWords=amtInWords+Str+" "+String.valueOf(placeValue.get(String.valueOf((partsOfReal.length-i)))) + " ";
					break;

				case 2:

					ltr = cntl.twonum(num);
					Str = Str + ltr;
					amtInWords=amtInWords+Str+" "+String.valueOf(placeValue.get(String.valueOf((partsOfReal.length-i))))+ " ";
					num=0;
					break;

				case 1:
					Str = Str + cntl.unitdo[num];
					amtInWords=amtInWords+Str+" "+String.valueOf(placeValue.get(String.valueOf((partsOfReal.length-i))))+ " ";
					num=0;
					break;

				}
			}
		}
		String paisaTotal="";
		//for(int i=0;i<srcAll[1].length();i++)
		if(Double.valueOf(srcAll[1])==0.00)
		{
			paisaTotal="Zero"+" ";
		}
		else
		{
			int len, q=0, r=0;
			String ltr = " ";
			String Str = "";
			CNTL cntl= new CNTL();
			int num = Integer.parseInt(srcAll[1]);


			if (num <= 0) System.out.println("Zero or Negative number not for conversion");

			while (num>0)
			{

				len = cntl.numberCount(num);

				//Take the length of the number and do letter conversion

				switch (len)

				{
				case 2:
					ltr = cntl.twonum(num);
					Str = Str + ltr;
					paisaTotal=paisaTotal+Str+" ";
					num=0;
					break;

				case 1:
					Str = Str + cntl.unitdo[num];
					paisaTotal=paisaTotal+Str+" ";
					num=0;
					break;


				}
			}
		}//else ends
		//System.out.println("zzzz..from outer part of if-else of paisa calc... the given input number "+src+" is equivalient to AMT in Words :"+amtInWords +"Rupees "+paisaTotal+" Paisa Only.");
		
		return (amtInWords.equals(""))?"" :""+amtInWords.trim()+" "+"Rupees "+(Double.valueOf(srcAll[1])==0.00 ? "":"AND "+paisaTotal+"Paisa ")+"Only" ;//+"Rupees AND "+srcAll[1]+" Paisa Only.";
	}//end of method 
	/**
	 * @param inputStr
	 * @return outPut 
	 * 
	 * First of All, this method is written to overcome the "LOSS OF PRECISION OF INPUT DATA" in
	 * operation of formatting numbers by DecimalFormats provided by java.text.DecimalFormat and
	 * in Format provided by com.ibm.icu.text.DecimalFormat, i.e. for example : when you
	 * try to format a really large number, it gets formatted into such number, which loses precision 
	 * [particularly in my test-cases, greater than ONE NEEL
	 * (Ref. to  Hindu-Arabic number place-values), the trailing real parts as well as imaginary(decimal) parts
	 * of input number lose precision, i.e. they get sometimes much greater values than they actually have,
	 *  and sometimes they are comverted much smaller values than they actual have. So, it got me sick, to try out
	 *  this one].
	 *  
	 * 
	 * Here, this method takes a numeric string @param (it may contain commas or may not; 
	 * BUT IT SHOULD NOT CONTAIN NON-NUMERIC CHAR(S) AND PERIOD i.e. DOT).
	 * If the source string<1000; no processing will be done.
	 * If the source string>=1000; processing for Hindu-Arabic Comma seperation, without loss
	 * of precision[].
	 * 
	 *  The scope of this method, has been opened to be public static, to be accessed from other side of development.
	 * 
	 */
	@SuppressWarnings("unused")
	public static String toHinduArabicFormat(String inputStr)
	{
		String outPut="";
		/* Here, size of array has statically been declared to be two.
		 * This is because, for our particular context, we are going to handle
		 * a really large decimal number containing real and decimal parts; and we
		 * aim that the real part of that number is hold by 0th index of this array
		 * and decimal part of that number is hold by 1st index of this array.
		 * */
		String srcAll[]=new String[2];
		if(inputStr.contains(","))
		{
			inputStr=inputStr.replaceAll(",", "");
		}
		/* Check if the number is a decimal(.) separated number or not.
		 * If the source number is a comma decimal separated one, split at first
		 * occurance of the decimal sign, into two arrays holding real part of number
		 * at index 0 and decimal part at 1 index of the array.
		 * */
		if(inputStr.contains("."))
		{
			srcAll=inputStr.split("[.]");
		}
		/* If the source numeric staring didn't contain dot(.),so,
		 *  it has only real parts. Assign the real and decimal parts 
		 *  of source number as follows then :
		 * */
		else
		{
			srcAll[0]=inputStr;
			srcAll[1]="00";
		}
		/* Some processing on decimal part of number.
		 * if the decimal part of number contains more than two digits,
		 * remove the very last digit of it.
		 * NOTE : HERE, WE ARE NOT GOING TO MAKE ANY MATH OPERAIONS ON IT,
		 * WE'LL SIMPLY TRUNCATE THE VERY LATER DIGITS OF IT.
		 * 
		 * THIS IS BECAUSE, IN MY BUSINESS REQUIREMENT, WE DO NOT NEED TO ROUND OFF;
		 * OR CEIL, OR FLOOR, JUST REMOVE THE TRAILING DIGITS, AFTER TWO DECIMAL DIGITS.
		 * */
		if(srcAll[1].length()>1)//index ranges as 0,1,2..... in string.
		{
			srcAll[1]=srcAll[1].substring(0,2);
		}
		/* Now, further processing is mainly for real part of source number.
		 * This if block is for case : number is Greater than equal to OR greater than Thousands.
		 * */		
		if(srcAll[0].length()>3)
		{
			char[]inputChrArray=srcAll[0].toCharArray();
			StringBuilder sbFirstBlock=new StringBuilder();
			StringBuilder sbRemainingBlock=new StringBuilder();;
			int j=0,k=0;
			for(int i=inputChrArray.length-1;i>=inputChrArray.length-3;i--)
			{
				j++;//counter for comma-separation in HA-Numeral system for very first block R to L
				sbFirstBlock.append(inputChrArray[i]);
				if(j==3)
				{
					j=0;//reset back it
					sbFirstBlock.append(',');
				}
			}//first for ends
			j=0;//reseed back value of j
			for(int i=inputChrArray.length-4;i>=0;i--)
			{
				j++;//counter for comma-separation in HA-Numeral system for rest of BLOCK R to L after first BLOCK
				sbRemainingBlock.append(inputChrArray[i]);
				if(j==2)
				{
					j=0;//reset back it
					sbRemainingBlock.append(',');
				}
			}//second for ends
			/*
			 * Because we'll reverse the content of StringBuilder later, the 
			 * un-necessary comma at very end of the the string should be removed
			 * before we'll truely reuse it in our toWordsMiracle(@param) method.
			 */
			if(sbRemainingBlock.toString().endsWith(","))
			{
				sbRemainingBlock=sbRemainingBlock.replace(sbRemainingBlock.lastIndexOf(","), sbRemainingBlock.lastIndexOf(",")+1, "");
			}
			//assign the processed value for output
			outPut=sbRemainingBlock.reverse().toString()+sbFirstBlock.reverse().toString()+"."+srcAll[1];
			System.out.println("kkk... from processing method... the Processed String builder has value="+outPut);
		}//outer if ends
		//number is smaller than one thousands
		else
		{
			//assign the processed value for output
			outPut=srcAll[0]+"."+srcAll[1];
			System.out.println("kkk.. from processing method... NO OP performed on input str="+srcAll[0]);
		}
		return outPut;
	}
	/**
	 * @param args
	 * 
	 * Have a testing on the methods
	 */ 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.out.println("From main method :"+new JrUtils().toWordsMiracle("997642945678903456.678"));
		//System.out.println("From main method, the toWordsMiracle() method's output :"+new JrUtils().toWordsMiracle("43578708965960234.678"));
		/*System.out.println("From main method, the toWordsMiracle() method's output :"+new JrUtils().toWordsMiracle("34.678"));
		System.out.println("From main method, the toWordsMiracle() method's output :"+new JrUtils().toWordsMiracle("4.678"));
		System.out.println("From main method, the toWordsMiracle() method's output :"+new JrUtils().toWordsMiracle("0004.678"));*/
		//System.out.println("From main method, from toHinduArabicformat() method :"+new JrUtils().toHinduArabicFormat("43,57,87,089,659,602,34.678"));
	}


}//end of class
/**
 * @author suresh
 * 
 * This class is used to figure out how the separated number should be spelled.
 *
 */
class CNTL
{
	String[] unitdo ={"", "One", "Two", "Three", "Four","Five",
			"Six", "Seven", "Eight", "Nine"};
	String[] tens =  {"", "One", "Two", "Three","Four", "Five",
			"Six","Seven", "Eight", "Nine","Ten","Eleven","Twelve", "Thirteen","Fourteen",
			"Fifteen","Sixteen","Seventeen","Eighteen","Nineteen",
			"Twenty","Twenty-One","Twenty-Two","Twenty-Three","Twenty-Four", 
			"Twenty-Five","Twenty-Six","Twenty-Seven","Twenty-Eight","Twenty-Nine",
			"Thirty","Thirty-One","Thirty-Two","Thirty-Three","Thirty-Four", 
			"Thirty-Five","Thirty-Six","Thirty-Seven","Thirty-Eight","Thirty-Nine",
			"Forty","Forty-One","Forty-Two","Forty-Three","Forty-Four", 
			"Forty-Five","Forty-Six","Forty-Seven","Forty-Eight","Forty-Nine", 
			"Fifty","Fifty-One","Fifty-Two","Fifty-Three","Fifty-Four", 
			"Fifty-Five","Fifty-Six","Fifty-Seven","Fifty-Eight","Fifty-Nine",	
			"Sixty","Sixty-One","Sixty-Two","Sixty-Three","Sixty-Four", 
			"Sixty-Five","Sixty-Six","Sixty-Seven","Sixty-Eight","Sixty-Nine", 
			"Seventy","Seventy-One","Seventy-Two","Seventy-Three","Seventy-Four", 
			"Seventy-Five","Seventy-Six","Seventy-Seven","Seventy-Eight","Seventy-Nine", 
			"Eighty","Eighty-One","Eighty-Two","Eighty-Three","Eighty-Four", 
			"Eighty-Five","Eighty-Six","Eighty-Seven","Eighty-Eight","Eighty-Nine",
			"Ninety","Ninety-One","Ninety-Two","Ninety-Three","Ninety-Four", 
			"Ninety-Five","Ninety-Six","Ninety-Seven","Ninety-Eight","Ninety-Nine"};
	String[] digit = {"", " "+"Hundred"+" ","Thousand","Lakh","Crore"};
	int r;


	//Count the number of digits in the input number
	int numberCount(int num)
	{
		int cnt=0;

		while (num>0)
		{
			r = num%10;
			cnt++;
			num = num / 10;
		}

		return cnt;
	}//end of method


	//method for Conversion of two digit

	@SuppressWarnings("unused")
	String twonum(int numq)
	{
		int numr, nq;
		String ltr="";

		//nq = numq / 10;
		/*numr = numq % 10;
         System.out.println("kkk  numq % 10 ="+numr);*/

		if (numq>9)
		{
			ltr=ltr+tens[numq];
			System.out.println("kkk  tens[numr] ="+ltr);
		}
		else
		{
			ltr = ltr+unitdo[numq];
		}

		return ltr;
	}//end of method

	//method for Conversion of three digit

	String threenum(int numq)
	{
		int numr, nq;
		String ltr = "";

		nq = numq / 100;
		numr = numq % 100;

		if (numr == 0)
		{
			ltr = ltr + unitdo[nq]+digit[1];
		}
		else
		{
			ltr = ltr +unitdo[nq]+digit[1]+""+twonum(numr);
		}
		return ltr;

	}//end of method
}//end of class