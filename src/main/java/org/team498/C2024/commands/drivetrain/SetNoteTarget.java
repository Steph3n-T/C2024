package org.team498.C2024.commands.drivetrain;

import org.team498.C2024.FieldPositions;
import org.team498.C2024.StateController;
import org.team498.lib.field.Point;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class SetNoteTarget extends InstantCommand{
    private final Point note;

    public SetNoteTarget(Point note){
        this.note = note;
    }

    @Override
    public void initialize() {
        if(note == null) {
            StateController.getInstance().setNote(FieldPositions.midNotes[0]);
            StateController.getInstance().setTargetDrive(null);
        }
        else {
            StateController.getInstance().setNote(note);
            StateController.getInstance().setTargetDrive(note.toPose2d());
        }
    }
}
