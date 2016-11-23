package com.ningsheng.jietong.Utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.Toast;

import com.ningsheng.jietong.App.MyApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 张恒 on 2015/8/28.
 */
public class TextFilter {
    private static abstract class NormTextFilter implements InputFilter {
        private boolean isGetParameter = true;
        private String toastText = "";

        public boolean isGetParameter() {
            return isGetParameter;
        }

        public void setGetParameter(boolean getParameter) {
            isGetParameter = getParameter;
        }

        public String getToastText() {
            return toastText;
        }

        public void setToastText(String toastText) {
            this.toastText = toastText;
        }
    }

    /**
     * 通用金额限制,小数点后只能有2位
     */
    public static class CurrencyTextFilter extends NormTextFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            String str = dest.toString();
            if (source.equals("")) {
                return null;
            }

            if (str.contains(".")) {
                if (str.subSequence(str.indexOf("."), str.length() - 1)
                        .length() == 1 && "0".equals(source.toString())) {
                    return dest.subSequence(dstart, dend);
                }
                if (str.subSequence(str.indexOf("."), str.length() - 1)
                        .length() == 2) {
                    return dest.subSequence(dstart, dend);
                }
            }

            return dest.subSequence(dstart, dend) + source.toString();
        }
    }

    /**
     * 通用金额限制
     */
    public static class NonzeroTextFilter extends NormTextFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            String str = dest.toString();
            if (source.equals("")) {
                return null;
            }
            if (dest.length() == 0 && source.toString().equals("0")) {
                Toast.makeText(MyApplication.getInstance(), "您输入的金额不对喔", Toast.LENGTH_SHORT).show();
                return dest.subSequence(dstart, dend);
            }

            return dest.subSequence(dstart, dend) + source.toString();
        }
    }

    /**
     * 没有获得限额的参数,不允许输入,并且提示错误
     */
    public static class NonzeroInHuiTianLiTextFilter extends NormTextFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            String str = dest.toString();
            if (source.equals("")) {
                return null;
            }
            if (!isGetParameter()) {
                if (!TextUtils.isEmpty(getToastText())) {
                    Toast.makeText(MyApplication.getInstance(), getToastText(), Toast.LENGTH_SHORT).show();
                }
            } else if (dest.length() == 0 && source.toString().equals("0")) {
                Toast.makeText(MyApplication.getInstance(), "您输入的金额不对喔", Toast.LENGTH_SHORT).show();
                return dest.subSequence(dstart, dend);
            }

            return dest.subSequence(dstart, dend) + source.toString();
        }
    }


    /**
     * 用于用户名限制
     */
    public static class UserNameTextFilter extends NormTextFilter {

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            String str = dest.toString();
            if (source.equals("")) {
                return null;
            }
            if (!isUserName(source.toString())) {
                Toast.makeText(MyApplication.getInstance(), "您输入的字符不正确喔", Toast.LENGTH_SHORT).show();
                return dest.subSequence(dstart, dend);
            } else {
                return null;
            }
        }
    }

    /**
     * 用于手机号限制
     */
    public static class MobileTextFilter extends NormTextFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            String str = dest.toString();
            if (source.equals("")) {
                return null;
            } else if (!isGetParameter()) {
                if (!TextUtils.isEmpty(getToastText())) {
                    Toast.makeText(MyApplication.getInstance(), getToastText(), Toast.LENGTH_SHORT).show();
                }
            } else if (dest.length() == 0 && !"1".equals(source)) {
                Toast.makeText(MyApplication.getInstance(), "首位只能是1喔", Toast.LENGTH_SHORT).show();
                return dest.subSequence(dstart, dend);
            }
            return dest.subSequence(dstart, dend) + source.toString();
        }
    }

    /**
     * 用于身份证限制
     */
    public static class IdCardTextFilter extends NormTextFilter {

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            String str = dest.toString();
            if (source.equals("")) {
                return null;
            }
            if (!isIdCard(source.toString())) {
                Toast.makeText(MyApplication.getInstance(), "您输入的字符不正确喔", Toast.LENGTH_SHORT).show();
                return dest.subSequence(dstart, dend);
            } else {
                return null;
            }
        }
    }

    /**
     * 用于登录密码限制
     */
    public static class PasswordTextFilter extends NormTextFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            String str = dest.toString();
            System.out.println("source: " + source.toString() + "   dest: " + dest.toString());
            if (source.equals("")) {
                return null;
            } else if (!isGetParameter()) {
                if (!TextUtils.isEmpty(getToastText())) {
                    Toast.makeText(MyApplication.getInstance(), getToastText(), Toast.LENGTH_SHORT).show();
                }
            } else if (dest.length() == 0 && !isPasswordTextFilter(source.toString())) {
                Toast.makeText(MyApplication.getInstance(), "密码必须以字母开头喔", Toast.LENGTH_SHORT).show();
                return dest.subSequence(dstart, dend);
            }
            return dest.subSequence(dstart, dend) + source.toString();
        }
    }


    /**
     * 用于支付密码限制
     */
    public static class PayPasswordTextFilter extends NormTextFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            System.out.println("source: " + source.toString() + "   dest: " + dest.toString());
            if (source.equals("")) {
                return null;
            } else if (!isGetParameter()) {
                if (!TextUtils.isEmpty(getToastText())) {
                    Toast.makeText(MyApplication.getInstance(), getToastText(), Toast.LENGTH_SHORT).show();
                }
            } else if (!isPayPassword(dest.toString() + source.toString())) {
                Toast.makeText(MyApplication.getInstance(), "只能输入数字和字母喔", Toast.LENGTH_SHORT).show();
                return dest.subSequence(dstart, dend);
            }
            return dest.subSequence(dstart, dend) + source.toString();
        }
    }

    /**
     * 检测用户名是否符合规定
     * 1、用户名：
     * （1）只允许字母/数字/中文组成；字母区分大小写；
     * （2）2-8个字符；
     *
     * @return
     */
    public static boolean isUserName(String userName) {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9\u4e00-\u9fa5']*$)");
        Matcher matcher = pattern.matcher(userName);
        return matcher.matches();

    }

    public static boolean isChinese(String chinese) {
        Pattern pattern = Pattern.compile("^([\u4e00-\u9fa5]*$)");
        Matcher matcher = pattern.matcher(chinese);
        return matcher.matches();
    }

    /**
     * 检测用户输入的密码是否符合规范
     * 1.密码
     * （1）包含字母，数字或字符至少两种组合，字母区分大小写；
     * （2）首位只能是字母；
     * （3）6-16个字符；
     *
     * @param password
     * @return
     */
    public static boolean isLoginPassword(String password) {
        Pattern pattern = Pattern.compile("^([a-zA-Z]*+[`a-zA-Z]*$)");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean isPasswordTextFilter(String password) {
        Pattern pattern = Pattern.compile("^([a-zA-Z]+[a-zA-Z0-9]*$)");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean isIdCard(String idCard) {
        Pattern pattern = Pattern.compile("^([0-9Xx*]*$)");
        Matcher matcher = pattern.matcher(idCard);
        return matcher.matches();
    }

    /**
     * @param password
     * @return
     */
    public static boolean isPayPassword(String password) {
        Pattern pattern = Pattern.compile("^([a-zA-Z]+[a-zA-Z0-9]*$)");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
