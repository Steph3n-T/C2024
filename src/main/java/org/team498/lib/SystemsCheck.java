package org.team498.lib;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;
import org.team498.lib.drivers.Blinkin;
import org.team498.lib.drivers.Blinkin.BlinkinColor;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BooleanSupplier;

import static edu.wpi.first.wpilibj2.command.Commands.*;

public class SystemsCheck {
    private final LinkedList<TestableObject> tests = new LinkedList<>();
    private final ShuffleboardTab tab;
    private final Command resetCommand;

    public SystemsCheck(String name, Command resetCommand, TestableObject... tests) {
        this.tests.addAll(List.of(tests));
        tab = Shuffleboard.getTab("Systems Check " + name);
        this.resetCommand = resetCommand;

        displayOnDashboard();
    }

    public SystemsCheck(String name, TestableObject... tests) {
        this(name, none(), tests);
    }

    private void displayOnDashboard() {
        for (TestableObject test : tests) {
            var element = test.name.split("/");
            var layout = tab.getLayout(element[0], "List Layout").withSize(1, 5);
            layout.addBoolean(element[1], () -> test.hasPassed);
        }
    }

    public Command test() {
        var testCommands = new LinkedList<Command>();
        for (TestableObject test : tests) {
            test.hasPassed = false;
            testCommands.add(getTestCommand(test));
        }
        testCommands.add(resetCommand);
        return sequence(testCommands.toArray(new Command[testCommands.size()]));
    }

    private Command getTestCommand(TestableObject test) {
        return sequence(
                runOnce(() -> Blinkin.getInstance().setColor(BlinkinColor.SOLID_YELLOW)),
                race(
                        sequence(
                                waitSeconds(test.timeout),
                                runOnce(() -> {
                                    test.hasPassed = false;
                                    Blinkin.getInstance().setColor(BlinkinColor.SOLID_RED);
                                })),
                        sequence(
                                waitUntil(test.isWorking),
                                runOnce(() -> {
                                    test.hasPassed = true;
                                    Blinkin.getInstance().setColor(BlinkinColor.SOLID_LIME);
                                })),
                        run(test.runnable),
                        waitSeconds(0.5)));
    }

    public static class TestableObject {
        public final String name;
        public final Runnable runnable;
        public final BooleanSupplier isWorking;
        public final double timeout;
        public boolean hasPassed;

        public TestableObject(String name, Runnable test, BooleanSupplier isWorking, double timeout) {
            this.name = name;
            this.runnable = test;
            this.isWorking = isWorking;
            this.timeout = timeout;
        }
    }
}
