package net.hisme.masaki.kyoani.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * アカウント情報
 * 
 * @author masaki
 * 
 */
public class Account {
    private String user_id = null;
    private String password = null;
    private boolean loaded = false;

    /**
     * Create Account instance
     */
    public Account() {
    }

    /**
     * create Account instance with Context and load
     * 
     * @param context
     * @throws BlankException
     *             アカウント情報が設定されていない場合
     */
    public Account(Context context) throws BlankException {
        this.load(context);
    }

    /**
     * load account information from SharedPreference
     * 
     * @param context
     * @return success?
     * @throws BlankException
     */
    public boolean load(Context context) throws BlankException {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(context);
        user_id = pref.getString("account", null);
        password = pref.getString("password", null);
        if (user_id == null || password == null || user_id == ""
                || password == "") {
            throw new BlankException();

        }
        loaded = true;
        return true;
    }

    /**
     * get user_id
     * 
     * @return user_id
     */
    public String getUser() {
        return this.user_id;
    }

    /**
     * get password
     * 
     * @return password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * アカウント情報が設定されていないときに発生する例外
     * 
     * @author masaki
     * 
     */
    public static class BlankException extends Exception {
        private static final long serialVersionUID = 1L;
    }
}
