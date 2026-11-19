/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/**
 * Palīgklase paroļu jaucējvērtību (hash) veidošanai.
 * Izmanto SHA-256 algoritmu, lai pārveidotu paroli
 * heksadecimālā formāta virknē.
 *
 * @author Nikita.Sersts
 */
public class PasswordUtil {

    /**
     * Izveido ievadītās paroles SHA-256 jaucējvērtību.
     *
     * @param password parole, kuru nepieciešams pārveidot
     * @return paroles SHA-256 jaucējvērtība heksadecimālā formātā
     * @throws RuntimeException ja SHA-256 algoritms nav pieejams
     */
    public static String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(
                    password.getBytes(StandardCharsets.UTF_8));

            StringBuilder result = new StringBuilder();

            for (byte b : bytes) {
                result.append(String.format("%02x", b & 0xff));
            }

            return result.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 nav pieejams", e);
        }
    }
}