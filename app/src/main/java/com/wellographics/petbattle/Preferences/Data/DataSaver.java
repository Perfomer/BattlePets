package com.wellographics.petbattle.Preferences.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.wellographics.petbattle.Objects.Pet;
import com.wellographics.petbattle.Objects.PlayerHero;

public class DataSaver extends DataOperator {

    private SharedPreferences.Editor pSHEditor;
    private PlayerHero pPlayerHero;

    public DataSaver(Context context) {
        super(context);
        pSHEditor = pDataOperator.edit();
    }

    public void setHero(PlayerHero hero) {
        pPlayerHero = hero;
    }

    /* МЕТОД, ВЫДАЮЩИЙ ДИНАМИЧНУЮ ИНФОРМАЦИЮ О ПИТОМЦАХ В СТРОКЕ:
     *  Формат сохранения данных: ИДЕНТИФИКАТОР_ПИТОМЦА!#КОЛИЧЕСТВО_УБИЙСТВ#ВЫБРАННЫЕ ЗАКЛИНАНИЯ(x3)#;
     *  Если питомец выбран, как основной боевой, то после его идентификатора будет "!";
     */
    private String getPetsDataString() {
        Pet[] petList = pPlayerHero.getAllPets();
        int actualPets[] = pPlayerHero.getActualPets(), foundActualPets = 0;
        String infoString = "";
        for (Pet x : petList) {
            int identifier = x.getIdentifier();
            infoString += String.valueOf(identifier);

            if (foundActualPets < actualPets.length)
                for (int i : actualPets) // Поиск и отметка выбранных питомцев
                    if (i == identifier) {
                        infoString += MARKER;
                        foundActualPets++;
                        break;
                    }

            infoString += SEPARATOR + String.valueOf(x.getNumberOfKills()) + SEPARATOR;

            int[] actualSpells = x.getActualSpellsIdentifierList();
            for (int i = 0; i < actualSpells.length; i++) {
                infoString += String.valueOf(actualSpells[i]);
                if (i != actualSpells.length - 1) infoString += SEPARATOR;
            }

            infoString += SUBSEPARATOR;
        }
        return infoString;
    }


    /* МЕТОД, СОХРАНЯЮЩИЙ ВСЮ ИНФОРМАЦИЮ О ГЕРОЕ:
     *  Формат сохранения денег: МОНЕТЫ#КАМНИ
     *  Формат сохранения уровня: УРОВЕНЬ#ОПЫТ
     *  Формат сохранения питомцев: ИДЕНТИФИКАТОР_ПИТОМЦА!#КОЛИЧЕСТВО_УБИЙСТВ#ВЫБРАННЫЕ ЗАКЛИНАНИЯ(x3);
     *   — Если питомец выбран, как основной боевой, то после его идентификатора будет "!"
     *  Формат сохранения последнего пройденного уровня: ИДЕНТИФИКАТОР_УРОВНЯ
     *  Перед использованием рекомендуется обновить переменную героя: setHero(PlayerHero hero);
     *  Использовать commit() нет необходимости.
     */
    public void saveData(PlayerHero hero) {
        setHero(hero);
        saveData(STATS_MONEY);
        saveData(STATS_LEVEL);
        saveData(PETS_ACTUAL);
        saveData(STAGES_COMPLETED);
        commit();
    }

    /* МЕТОД, СОХРАНЯЮЩИЙ КОНКРЕТНУЮ ИНФОРМАЦИЮ
     *  После сохранения всего, что необходимо, следует подтвердить сохранение: commit();
     * */
    private void saveData(String key) {
        String value = "";
        switch (key) {
            case STATS_MONEY: value = String.valueOf(pPlayerHero.getCoins()) + SEPARATOR +
                    String.valueOf(pPlayerHero.getGems());
                break;
            case STATS_LEVEL: value = String.valueOf(pPlayerHero.getLevel().getLevelValue()) +
                    SEPARATOR + String.valueOf(pPlayerHero.getLevel().getExperienceValue());
                break;
            case PETS_ACTUAL: value = getPetsDataString();
                break;
            case STAGES_COMPLETED: value = String.valueOf(pPlayerHero.getLastCompletedStage());
                break;
            default:
                Toast.makeText(pContext, "INVALID KEY FOR SAVING DATA: " + key, Toast.LENGTH_LONG).show();
        }
        putOnSave(key, value);
    }
    
    private void putOnSave(String key, String value) {
        pSHEditor.putString(key, value);
    }

    /* МЕТОД ПОДТВЕРЖДЕНИЯ СОХРАНЕНИЯ ДАННЫХ
     *  Выполняется пользователем по завершению отправки данных на сохранение */
    public void commit() {
        pSHEditor.commit();
    }


}
