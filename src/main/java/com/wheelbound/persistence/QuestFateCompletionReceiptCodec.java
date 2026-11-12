package com.wheelbound.persistence;

import com.google.gson.JsonObject;
import com.wheelbound.domain.QuestFateCompletionReceipt;
import com.wheelbound.domain.QuestFateCurrentAssignment;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Nested supplied receipt payload, independent of the run schema. Decode requires
 * an explicitly retained historical active context at the receipt revision, not
 * today's head. Neither the payload nor that supplied context authenticates
 * evidence, consumes an event, or authorizes a write or reward.
 */
final class QuestFateCompletionReceiptCodec
{
    private QuestFateCompletionReceiptCodec() { }

    static JsonObject encode(QuestFateCompletionReceipt receipt)
    {
        Objects.requireNonNull(receipt, "receipt");
        JsonObject value = new JsonObject();
        value.addProperty("version", 1);
        value.add("assignment", QuestFateAssignmentCodec.encode(receipt.assignment()));
        value.add("window", QuestFateObservationWindowCodec.encode(receipt.window(),
            receipt.assignment().provenance().reward().selection()));
        value.addProperty("expectedRevision", receipt.expectedRevision());
        value.addProperty("observedPosition", receipt.observedPosition());
        value.addProperty("commandId", receipt.commandId().toString());
        value.addProperty("sourceNamespace", receipt.sourceNamespace());
        value.addProperty("eventKey", receipt.eventKey());
        return value;
    }

    static QuestFateCompletionReceipt decode(JsonObject value,
        QuestFateCurrentAssignment retained)
    {
        Objects.requireNonNull(retained, "retained");
        RunCodec.keys(value, Set.of("version", "assignment", "window", "expectedRevision",
            "observedPosition", "commandId", "sourceNamespace", "eventKey"));
        if (RunCodec.integer(value, "version") != 1)
        { throw new IllegalArgumentException("Unsupported Questing receipt version"); }
        if (!value.get("assignment").isJsonObject() || !value.get("window").isJsonObject())
        { throw new IllegalArgumentException("Expected Questing assignment and window objects"); }
        var assignment = QuestFateAssignmentCodec.decode(value.getAsJsonObject("assignment"),
            retained.assignment().allocation());
        if (!assignment.equals(retained.assignment()))
        { throw new IllegalArgumentException("Changed retained Questing commitment"); }
        var window = QuestFateObservationWindowCodec.decode(value.getAsJsonObject("window"),
            assignment.provenance().reward().selection());
        String text = RunCodec.string(value, "commandId");
        UUID command = UUID.fromString(text);
        if (!command.toString().equals(text))
        { throw new IllegalArgumentException("Expected canonical Questing command UUID"); }
        try
        {
            return new QuestFateCompletionReceipt(retained, window,
                RunCodec.integer(value, "expectedRevision"), RunCodec.integer(value, "observedPosition"),
                command, RunCodec.string(value, "sourceNamespace"), RunCodec.string(value, "eventKey"));
        }
        catch (IllegalStateException ex)
        {
            // Domain lifecycle/transition failures make this supplied payload invalid.
            throw new IllegalArgumentException("Invalid Questing completion receipt context", ex);
        }
    }
}
