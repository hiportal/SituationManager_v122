package com.ex.gongsa;

import android.util.Log;

public  class Common {


    /**
     * 문자형 Validation 체크
     * @param str
     * @return
     */
    public static String nullCheck(String str){
        try{
            if(str.equals("") || str.length()==0 ){
                return "";
            }else{
                return str;
            }
        }catch (NullPointerException e){
            //e.printStackTrace();
            return "";
        }catch(Exception e){
            Log.e("에러","Exception");
            return "";
        }
    }//nullCheck(String str)

    /**
     * 더블형 Validation 체크
     * @param dou
     * @return
     */
    public static Double doubleNullCheck(Double dou){
        Log.i("공사구간:",Double.toString(dou));
        try{
            if(dou.equals("") || dou.equals(null)){
                return 0.0;
            }else{
                return dou;
            }
        }catch (NullPointerException  e){
            return 0.0;
        }catch(NumberFormatException e){
            Log.e("에러","Exception");
            return 0.0;
        }catch(Exception e){
            Log.e("에러","Exception");
            return 0.0;
        }
    }

    public static int intStrNullCheck(String number){
        try{
            if(number.equals("") || number.length()==0 ){
                return 0;
            }else{
                return Integer.parseInt(number);
            }
        }catch (NullPointerException  e){
            Log.e("에러","Exception");
            //e.printStackTrace();
            return 0;
        }catch ( NumberFormatException e){
            Log.e("에러","Exception");
            //e.printStackTrace();
            return 0;
        }catch ( Exception e){
            Log.e("에러","Exception");
            //e.printStackTrace();
            return 0;
        }
    }

}
