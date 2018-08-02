package com.hnxjgou.xinjia.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class
        StringUtil {
	/**
	 * 手机号码的验证，严格验证
	 * 
	 * @param mobiles
	 *            要验证的手机号码
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		if (mobiles == null) {
			return false;
		}
		Pattern p = Pattern
				.compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(17[0,6,7,8])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	
	 /** 
     * 电话号码验证 
     * @param  str
     * @return 验证通过返回true 
     */  
    public static boolean isPhone(String str) { 
    	if (str == null) {
			return false;
		}
        Pattern p1 = null,p2 = null;  
        Matcher m = null;  
        boolean b = false;    
        p1 = Pattern.compile("[0,4,8]{1}[0-9]{2,3}[-]{0,1}[0-9]{7,8}");  // 验证带区号的  
        p2 = Pattern.compile("^[1-9]{1}[0-9]{4,8}$");         // 验证没有区号的  
        if(str.length() >9)  
        {   m = p1.matcher(str);  
            b = m.matches();    
        }else{  
            m = p2.matcher(str);  
            b = m.matches();   
        }    
        return b;  
    }  

	/**
	 * E_mail的验证
	 * 
	 * @param email
	 *            要验证的email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (email == null) {
			return false;
		}
		String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * 验证qq是否是合法QQ
	 * 
	 * @param qq
	 * @return
	 */
	public static boolean isQQ(String qq) {
		if (qq == null) {
			return false;
		}
		String str = "^[1-9][0-9]{4,}$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(qq);
		return m.matches();
	}

    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }

                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

}
