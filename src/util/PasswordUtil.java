/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author Nikita.Sersts
 */
public class PasswordUtil {

    public static String hash(String password) {
        return Integer.toHexString(password.hashCode()); 
    }
}
