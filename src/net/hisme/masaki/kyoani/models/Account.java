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

    /**
     * SharedPreferenceに保存されたデータを読み取ってAccountを作る
     * 
     * @param context
     * @return Accountのインスタンス
     * @throws BlankException
     *             アカウント情報が設定されていない場合
     */
    public static Account load(Context context) throws BlankException {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(context);
        String user_id = pref.getString("account", null);
        String password = pref.getString("password", null);
        if (user_id == null || password == null || user_id == ""
                || password == "") {
            throw new BlankException();
        }
        return new Account(user_id, password);
    }

    private Account(String user_id, String password) {
        this.user_id = user_id;
        this.password = password;
    }

    /**
     * ユーザ名を取得
     * 
     * @return
     */
    public String getUser() {
        return this.user_id;
    }

    /**
     * パスワードを取得
     * 
     * @return
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
