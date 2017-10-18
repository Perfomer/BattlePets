package com.wellographics.petbattle.Objects.Stuff;

import android.content.Context;

/**
 * ТИПЫ ПИТОМЦЕВ ПО ИДЕНТИФИКАТОРУ:
 * [-1 — null pet]
 * 0 — Гуманоид
 * 1 — Магические
 * 2 — Летающие
 * 3 — Водные
 * 4 — Неживые
 * 5 — Животные
 */

public class PetType {

    private int ptTypeIdentifier;

    public static final int TP_NULL = -1, TP_HUMANOID = 0, TP_MAGIC = 1, TP_FLYABLE = 2,
            TP_WATER = 3, TP_INANIMATE = 4, TP_ANIMAL = 5;

    public static final float ANTAGONIST_COEFF = 0.33f, PROTAGONIST_COEFF = 0.5f;

    public PetType(int typeIdentifier) {
        ptTypeIdentifier = typeIdentifier;
    }

    /**
     * МЕТОД ПОЛУЧЕНИЯ ИДЕНТИФИКАТОРА ТИПА ПИТОМЦЕВ-АНТАГОНИСТОВ
     * Урон по питомцам-антагонистам снижен на ANTAGONIST_COEFF
     *
     * @return antType — идентификатор типа питомцев-антагонистов
     */
    public int getAntagonistTypeIdentifier() {
        switch (ptTypeIdentifier) {
            case TP_HUMANOID:
                return TP_ANIMAL;
            case TP_MAGIC:
                return TP_WATER;
            case TP_FLYABLE:
                return TP_MAGIC;
            case TP_WATER:
                return TP_INANIMATE;
            case TP_INANIMATE:
                return TP_FLYABLE;
            case TP_ANIMAL:
                return TP_HUMANOID;
            default:
                return TP_NULL;
        }
    }

    /**
     * МЕТОД ПОЛУЧЕНИЯ ИДЕНТИФИКАТОРА ТИПА ПИТОМЦЕВ-ПРОТАГОНИСТОВ
     * Урон по питомцам-протагонистам увеличен на PROTAGONIST_COEFF
     *
     * @return proType — идентификатор типа питомцев-протагонистов
     */
    public int getProtagonistTypeIdentifier() {
        switch (ptTypeIdentifier) {
            case TP_HUMANOID:
                return TP_INANIMATE;
            case TP_MAGIC:
                return TP_FLYABLE;
            case TP_FLYABLE:
                return TP_HUMANOID;
            case TP_WATER:
                return TP_MAGIC;
            case TP_INANIMATE:
                return TP_ANIMAL;
            case TP_ANIMAL:
                return TP_WATER;
            default:
                return TP_NULL;
        }
    }

    public int getTypeIdentifier() {
        return ptTypeIdentifier;
    }

    public int getTypeIcon(Context context) {
        final String PATH = "general_pettype_" + String.valueOf(ptTypeIdentifier);
        int drawableId =
                context.getResources().getIdentifier(PATH, "drawable", context.getPackageName());
        return drawableId;
    }
}
