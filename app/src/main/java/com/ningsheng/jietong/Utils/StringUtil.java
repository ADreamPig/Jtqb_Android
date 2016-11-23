package com.ningsheng.jietong.Utils;

import android.text.Html;
import android.text.Spanned;
import android.widget.EditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * 
 * @author M.c
 * 
 * @isBlank 判断字符串是否为空
 * @isChinese 判断是否为中文
 * @countStringLength 判断字符串是否超出长度
 * @isNum 判断是否为纯数字
 * @DoubleToAmountString double转为字符串,保留小数位
 * @removeAllChar 去掉字符串中某一字符
 * @getInitialAlphaEn 获取英文首字母 并大写显示,不为英文字母时,返回"#"
 * @getEditText 获取非空edittext
 * @getMd5Value 字符串MD5加密
 */
public class StringUtil {


    /**
     * 26英文字母字符串
     */
    public static String[] ENGLIST_LETTER = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

	/**
	 * 判断字符串是否为空或者空字符串 如果字符串是空或空字符串则返回true，否则返回false。也可以使用Android自带的TextUtil
	 * 
	 * @param str
	 * @return 
	 */

	public static boolean isEmpty(String s) {
		if (null == s)
			return true;
		if (s.length() == 0)
			return true;
		if (s.trim().length() == 0)
			return true;
		return false;
	}
	public static boolean isBlank(String str) {
		if (str == null || "".equals(str) || str.trim().length() == 0||"null".equals(str)) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean isBlank(String... str) {
		if(str == null){
			return true;
		}
		boolean isTrue = true;
		for (int i = 0; i < str.length; i++) {
			System.out.print("==========="+str[i]);
			if (str[i] == null  || str[i].length() == 0) {
				return true;
			} else {
				isTrue = false;
			}
		}
		return isTrue;
		
	}

	/**
	 * 判断是否是中文
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/**
	 * 判断字符串是否超过指定字符数(待测试)
	 * 
	 * @param content
	 * @param stringNum
	 *            指定字符数 如：140
	 * @return
	 */
	public static boolean countStringLength(String content, int stringNum) {
		int result = 0;
		if (content != null && !"".equals(content)) {
			char[] contentArr = content.toCharArray();
			if (contentArr != null) {
				for (int i = 0; i < contentArr.length; i++) {
					char c = contentArr[i];
					if (isChinese(c)) {
						result += 3;
					} else {
						result += 1;
					}
				}
			}
		}
		if (result > stringNum * 3) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断字符串是否为无符号数字
	 * 
	 * @param num
	 * @return
	 */
	public static boolean isNum(String num) {
		String check = "^[0-9]*$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(num);
		boolean isMatched = matcher.matches();
		return isMatched;
	}

	/**
	 * 将double转换为字符串，保留小数点位数
	 * 
	 * @param doubleNum
	 *            需要解析的double
	 * @param digitNum
	 *            小数点位数，小于0则默认0
	 * @return
	 */
	public static String DoubleToAmountString(Double doubleNum, int digitNum) {
		if (digitNum < 0)
			digitNum = 0;
		StringBuilder strBuilder = new StringBuilder("#");
		for (int i = 0; i < digitNum; i++) {
			if (i == 0)
				strBuilder.append(".#");
			else
				strBuilder.append("#");
		}
		DecimalFormat df = new DecimalFormat(strBuilder.toString());
		return df.format(doubleNum);
	}

	/**
	 * 提取英文的首字母，非英文字母用#代替
	 * 
	 * @param str
	 * @return
	 */
	public static String getInitialAlphaEn(String str) {
		if (str == null) {
			return "#";
		}

		if (str.trim().length() == 0) {
			return "#";
		}

		char c = str.trim().substring(0, 1).charAt(0);
		// 正则表达式，判断首字母是否是26字母
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase(Locale.getDefault()); // 大写输出
		} else {
			return "#";
		}
	}


	/**
	 * 去除String中的某一个字符
	 * 
	 * @param orgStr
	 * @param splitStr
	 *            要去除的字符串
	 * @return
	 */
	public static String removeAllChar(String orgStr, String splitStr) {
		String[] strArray = orgStr.split(splitStr);
		String res = "";
		for (String tmp : strArray) {
			res += tmp;
		}
		return res;
	}

	/**
	 * 获取非空edittext
	 * @param text
	 * @return
	 */
	public static String getEditText(EditText text) {
		if (null==text||text.getText().toString().trim().equals("")) {
			return "";
		}
		return text.getText().toString().trim();
	}

	/**
	 * MD5加密 32位小写
	 * @param sSecret 
	 * @return
	 */
	public static String getMd5Value(String sSecret) {
		try {
			MessageDigest bmd5 = MessageDigest.getInstance("MD5");
			bmd5.update(sSecret.getBytes());
			int i;
			StringBuffer buf = new StringBuffer();
			byte[] b = bmd5.digest();
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	public static Spanned getReplyString() {
		  String ReplyFormat = "<font color='#959595'> 海外手机请加当地国码,去除第一位的0,例如台湾手机：</font><font color='#C30000'>886</font><font color='#959595'>935888999</font>";
		  return Html.fromHtml(ReplyFormat);
//		return Html.fromHtml(ReplyFormat.replace("person1", person1)
//				.replace("person2", person2).replace("content", content));
	}


	/**
	 * 时间截取
	 *
	 * @param datetime
	 * @param splite
	 * @return
	 */
	public static int[] getYMDArray(String datetime, String splite) {
		int date[] = {0, 0, 0, 0, 0};
		if (datetime != null && datetime.length() > 0) {
			String[] dates = datetime.split(splite);
			int position = 0;
			for (String temp : dates) {
				date[position] = Integer.valueOf(temp);
				position++;
			}
		}
		return date;
	}
//public static boolean isMobileNo(String mobile){
//	Pattern pt=Pattern.compile("^((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8]))//d{8}$");
//	Matcher ma=pt.matcher(mobile);
//	return ma.matches();
//}

	public static boolean isMobileNo(String mobiles) {

		if (isBlank(mobiles)) {
			return false;
		}

		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,3,1,5-9])|(17[6，7,8]))\\d{8}$");

		Matcher m = p.matcher(mobiles);

		return m.matches();

	}


}
