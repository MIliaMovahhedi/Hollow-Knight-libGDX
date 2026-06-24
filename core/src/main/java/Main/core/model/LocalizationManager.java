package Main.core.model;

import Main.core.model.enums.Language;
import Main.core.view.Translatable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;
import java.util.Locale;

public class LocalizationManager {

    private static LocalizationManager instance;
    private ArrayList<Translatable> translatableItems;
    private I18NBundle bundle;
    private Language currentLanguage;

    private LocalizationManager() {

        translatableItems =  new ArrayList<>();
        currentLanguage = Language.ENGLISH;
        changeLanguage(currentLanguage);
    }
    public static LocalizationManager getInstance() {
        if (instance == null) {
            instance = new LocalizationManager();
        }
        return instance;
    }

    public void register(Translatable translatable) {

        if(!translatableItems.contains(translatable)) {

            translatableItems.add(translatable);

            if (bundle != null) {

                translatable.onLanguageChanged(bundle);
            }
        }
    }
    public void unregister(Translatable translatable) {

        translatableItems.remove(translatable);
    }
    public void changeLanguage(Language newLanguage) {

        this.currentLanguage = newLanguage;
        Locale locale = newLanguage == Language.ENGLISH ? Locale.ENGLISH : Locale.GERMAN;

        bundle = I18NBundle.createBundle(Gdx.files.internal("i18n/messages"), locale);

        for (Translatable translatableItem : translatableItems ) {

            translatableItem.onLanguageChanged(bundle);
        }
    }

    public Language getCurrentLanguage() {
        return currentLanguage;
    }
}
