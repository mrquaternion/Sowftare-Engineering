package com.maville.controller.menu;

import com.maville.controller.services.Authenticate;
import com.maville.model.Intervenant;
import com.maville.view.AuthenticationView;
import com.maville.view.MenuView;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationMenu extends Menu {
    private Authenticate authenticate;

    // TODO : METTRE TOUT CA DANS AUTHENTICATIONVIEW
    private static final String[] LOGIN_INFO_MESSAGES = {
            "Adresse courriel",
            "Mot de passe"
    };
    private static final String[] SIGNUP_RESIDENT_INFO_MESSAGES = {
            "Nom complet",
            "Date de naissance (DD/MM/YYYY)",
            "Adresse courriel",
            "Mot de passe",
            "Numéro de téléphone (optionnel)",
            "Adresse résidentielle"
    };
    private static final String[] SIGNUP_INTERVENANT_INFO_MESSAGES = {
            "Nom complet",
            "Adresse courriel",
            "Mot de passe",
            "Identifiant de la ville"
    };

    // Logique de connexion
    public void logInManager() {
        while (true) {
            AuthenticationView.showAuthMessage();
            AuthenticationView.showAuthType();

            if (SCANNER.hasNextInt()) { // Vérifie si l'entrée est un entier
                int option = SCANNER.nextInt();
                selection(option, "login"); // Passe "login" en tant qu'argument pour indiquer la connexion
                break;
            } else {
                System.out.println("Entrée invalide. Veuillez entrer un numéro valide.");
                SCANNER.next(); // Consomme l'entrée incorrecte pour éviter une boucle infinie
            }
        }
    }

    // Logique d'inscription
    public void signUpManager() {
        while (true) {
            AuthenticationView.showAuthMessage();
            AuthenticationView.showAuthType();

            if (SCANNER.hasNextInt()) { // Vérifie si l'entrée est un entier
                int option = SCANNER.nextInt();
                selection(option, "signup"); // Passe "signup" en tant qu'argument pour indiquer la connexion
                break;
            } else {
                System.out.println("Entrée invalide. Veuillez entrer un numéro valide.");
                SCANNER.next(); // Consomme l'entrée incorrecte pour éviter une boucle infinie
            }
        }
    }

    // Implémentation de la logique de sélection selon l'action (connexion ou inscription) et le type d'utilisateur
    @Override
    protected void selection(int option, String action) {
        switch (action) {
            case "login":
                handleLogIn(option);
                break;
            case "signup":
                handleSignUp(option);
                break;
            default:
                System.out.println("Action inconnue.");
                break;
        }
    }

    private void handleLogIn(int option) {
        String userType;
        switch (option) {
            case 1:
                userType = "resident";
                continueProcess(userType);
                break;
            case 2:
                userType = "intervenant";
                continueProcess(userType);
                break;
            case 0:
                MenuView.backMessage();
                return;
            default:
        }
    }

    private void continueProcess(String userType) {
        AuthenticationView.showLogInMessage(userType);
        SCANNER.nextLine();
        while (true) {
            authenticate = new Authenticate(collectUserInfo(LOGIN_INFO_MESSAGES));

            if (authenticate.logIn()) { // Construction du User
                String userTypeFromDB = authenticate.getUserType();
                if (!userType.equals(userTypeFromDB)) { // Si le user essaye de se connecter en tant qu'un autre type de user
                    System.out.println("Vous n'êtes pas un " + userType);
                    return;
                }
                DefaultMenu.showUserMenu(userTypeFromDB);
                break;
            } else {
                return;
            }
        }
    }

    private void handleSignUp(int option) {
        String userType;
        String[] infoMessages;

        switch (option) {
            case 1:
                userType = "resident";
                infoMessages = SIGNUP_RESIDENT_INFO_MESSAGES;
                break;
            case 2:
                userType = "intervenant";
                infoMessages = SIGNUP_INTERVENANT_INFO_MESSAGES;
                break;
            case 0:
                MenuView.backMessage();
                return;
            default:
                System.out.println("Option invalide pour l'inscription.");
                return;
        }

        AuthenticationView.showSignUpMessage(userType);
        SCANNER.nextLine();

        authenticate = new Authenticate(collectUserInfo(infoMessages));
        if (authenticate.signUp(userType)) { // Construction du User
            DefaultMenu.showUserMenu(userType);
        }
    }

    private List<String> collectUserInfo(String[] infoMessages) {
        List<String> userInfo = new ArrayList<>();
        for (String message : infoMessages) {
            AuthenticationView.printMessage(message);
            String input = SCANNER.nextLine();
            userInfo.add(input);
        }

        return userInfo;
    }

    private static int getInput() {
        while (true) {
            try {
                return Integer.parseInt(SCANNER.nextLine().trim());
            } catch (NumberFormatException e) {
                AuthenticationView.showInvalidInputMessage();
            }
        }
    }

    public static int askForCompanyType() {
        Intervenant.CompanyType[] companyTypes = Intervenant.CompanyType.values();

        while (true) {
            AuthenticationView.showCompanyTypeMessage();
            AuthenticationView.showCompanyTypes(companyTypes);

            int choice = getInput();
            if (choice >= 1 && choice <= companyTypes.length) {
                return choice;
            } else {
                AuthenticationView.showInvalidChoiceMessage(companyTypes.length);
            }
        }
    }
}
