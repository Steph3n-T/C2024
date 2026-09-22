package org.team498.lib.drivers;

import static org.team498.C2024.Ports.Accessories.BLINKIN;

import edu.wpi.first.wpilibj.motorcontrol.Spark;

public class Blinkin {
    private final Spark blinkin;
    //got rid of the color it set by itself
    private BlinkinColor color;
    // private DigitalOutput digitalOutput = new DigitalOutput(BLINKIN);

    public Blinkin() {
        blinkin = new Spark(BLINKIN);
        blinkin.setSafetyEnabled(false);
        setColor(BlinkinColor.OCEAN_PALETTE);
    }

    public void setColor(BlinkinColor color) {
        this.color = color;
        set(color.val);
    }

    public BlinkinColor getColor() {
        return color;
    }

    public void set(double val) {
        if ((val >= -1.0) && (val <= 1.0)) {
            blinkin.set(val);
            // digitalOutput.pulse(((val * 500) + 1500) / 1e+6);
        }
    }

    private static Blinkin instance;

    public static Blinkin getInstance() {
        if (instance == null) {
            instance = new Blinkin();
        }
        return instance;
    }

    public enum BlinkinColor {
        RAINBOW_PALETTE(-99),
        PARTY_PALETTE(-97),
        OCEAN_PALETTE(-95),
        LAVA_PALETTE(-93),
        FOREST_PALETTE(-91),
        RAINBOW_WITH_GLITTER(-89),
        CONFETTI(-87),
        SHOT_RED(-85),
        SHOT_BLUE(-83),
        SHOT_WHITE(-81),
        SINELEON_RAINBOW_PALETTE(-79),
        SINELEON_PARTY_PALETTE(-77),
        SINELEON_OCEAN_PALETTE(-75),
        SINELEON_LAVA_PALETTE(-73),
        SINELEON_FOREST_PALETTE(-71),
        BEATS_PER_MINUTE_RAINBOW_PALETTE(-69),
        BEATS_PER_MINUTE_PARTY_PALETTE(-67),
        BEATS_PER_MINUTE_OCEAN_PALETTE(-65),
        BEATS_PER_MINUTE_LAVA_PALETTE(-63),
        BEATS_PER_MINUTE_FOREST_PALETTE(61),
        FIRE_MEDIUM(-59),
        FIRE_LARGE(-57),
        TWINKLES_RAINBOW_PALETTE(-55),
        TWINKLES_PARTY_PALETTE(-53),
        TWINKLES_OCEAN_PALETTE(-51),
        TWINKLES_LAVA_PALETTE(-49),
        TWINKLES_FOREST_PALETTE(-47),
        COLOR_WAVES_RAINBOW_PALETTE(-45),
        COLOR_WAVES_PARTY_PALETTE(-43),
        COLOR_WAVES_OCEAN_PALETTE(-41),
        COLOR_WAVES_LAVA_PALETTE(-39),
        COLOR_WAVES_FOREST_PALETTE(-37),
        LARSON_SCANNER_RED(-35),
        LARSON_SCANNER_GRAY(-33),
        LIGHT_CHASE_RED(-31),
        LIGHT_CHASE_BLUE(-29),
        LIGHT_CHASE_GRAY(-27),
        HEARTBEAT_RED(-25),
        HEARTBEAT_BLUE(-23),
        HEARTBEAT_WHITE(-21),
        HEARTBEAT_GRAY(-19),
        BREATH_RED(-17),
        BREATH_BLUE(-15),
        BREATH_GRAY(-13),
        STROBE_RED(-11),
        STROBE_BLUE(-9),
        STROBE_GOLD(-7),
        STROBE_WHITE(-5),
        COLOR_1_END_TO_END_BLACK_TO_BLACK(-3),
        COLOR_1_LARSON_SCANNER(-1),
        COLOR_1_LIGHT_CHASE(1),
        COLOR_1_HEARTBEAT_SLOW(3),
        COLOR_1_HEARBEAT_MEDIUM(5),
        COLOR_1_HEARTBEAT_FAST(7),
        COLOR_1_BREATH_SLOW(9),
        COLOR_1_BREATH_FAST(11),
        COLOR_1_SHOT(13),
        COLOR_1_STROBE(15),
        COLOR_2_END_TO_END_BLACK_TO_BLACK(17),
        COLOR_2_LARSON_SCANNER(19),
        COLOR_2_LIGHT_CHASE(21),
        COLOR_2_HEARTBEAT_SLOW(23),
        COLOR_2_HEARBEAT_MEDIUM(25),
        COLOR_2_HEARTBEAT_FAST(27),
        COLOR_2_BREATH_SLOW(29),
        COLOR_2_BREATH_FAST(31),
        COLOR_2_SHOT(33),
        COLOR_2_STROBE(35),
        COLOR_1_COLOR_2_SPARKLE_C1_ON_C2(37),
        COLOR_1_COLOR_2_SPARKLE_2_ON_C1(39),
        COLOR_1_COLOR_2_GRADIENT(41),
        COLOR_1_COLOR_2_BEATS_PER_MINUTE(43),
        COLOR_1_COLOR_2_END_TO_END_BLEND_1_TO_2(45),
        COLOR_1_COLOR_2_END_TO_END_BLEND(47),
        COLOR_1_COLOR_2_COLOR_1_AND_COLOR_2(49),
        COLOR_1_COLOR_2_TWINKLES(51),
        COLOR_1_COLOR_2_WAVES(53),
        COLOR_1_COLOR_2_SINELON(55),
        SOLID_HOT_PINK(57),
        SOLID_DARK_RED(59),
        SOLID_RED(61),
        SOLID_RED_ORANGE(63),
        SOLID_ORANGE(65),
        SOLID_GOLD(67),
        SOLID_YELLOW(69),
        SOLID_LAWN_GREEN(71),
        SOLID_LIME(73),
        SOLID_DARK_GREEN(75),
        SOLID_GREEN(77),
        SOLID_BLUE_GREEN(79),
        SOLID_AQUA(81),
        SOLID_SKY_BLUE(83),
        SOLID_DARK_BLUE(85),
        SOLID_BLUE(87),
        SOLID_BLUE_VIOLET(89),
        SOLID_VIOLET(91),
        SOLID_WHITE(93),
        SOLID_GRAY(95),
        SOLID_DARK_GRAY(97),
        SOLID_BLACK(99);

        public final double val;

        BlinkinColor(double val) {
            this.val = val / 100;
        }
    }
}