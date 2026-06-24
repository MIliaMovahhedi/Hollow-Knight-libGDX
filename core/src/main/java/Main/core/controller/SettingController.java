package Main.core.controller;

import Main.core.model.LocalizationManager;
import Main.core.model.enums.Language;
import Main.core.model.enums.ScreenState;
import Main.core.view.ScreenManager;

public class SettingController {

    public void goBackToMainMenu() {

        ScreenManager.getInstance().navigateTo(ScreenState.MAIN_MENU);
    }
    public void changeLanguage() {

        Language newLanguage = LocalizationManager.getInstance().getCurrentLanguage() == Language.ENGLISH ? Language.GERMAN :  Language.ENGLISH;
        LocalizationManager.getInstance().changeLanguage(newLanguage);
    }
}
