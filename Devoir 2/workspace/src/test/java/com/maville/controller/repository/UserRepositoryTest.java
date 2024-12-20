package com.maville.controller.repository;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryTest {
    @Test
    // Dans ce test, on regarde si le ID de l'utilisateur stocké dans la base des données correspond à ce qu'on fetch.
    public void fetchUserTest() {
        List<String> userInfo = new ArrayList<>();
        userInfo.add("john.doe@gmail.com"); // Adresse courriel
        userInfo.add("johndoe123"); // Mot de passe

        UserRepository userRepo = UserRepository.getInstance();
        String[] importantInfo = userRepo.fetchUser(userInfo);
        String userIdFetched = importantInfo[0];

        assertEquals("755bf798-da16-4116-95e3-9fae9a220037", userIdFetched);
    }
    // Ce test vérifie le comportement de fetchUser si le mdp entré est erroné
    @Test
    public void fetchUserInvalidPasswordTest() {
        List<String> userInfo = new ArrayList<>();
        userInfo.add("john.doe@gmail.com");
        userInfo.add("mauvaismdp");

        UserRepository userRepo = UserRepository.getInstance();
        String[] importantInfo = userRepo.fetchUser(userInfo);

        assertNull("Devrais être vide", importantInfo);
    }
}
