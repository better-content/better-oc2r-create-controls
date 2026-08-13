package com.bettercontent.oc2rcreatebridge.compat.adapters.kinetics

import com.bettercontent.oc2rcreatebridge.compat.errors.CreatePeripheralException
import com.simibubi.create.content.kinetics.transmission.sequencer.Instruction
import com.simibubi.create.content.kinetics.transmission.sequencer.InstructionSpeedModifiers
import com.simibubi.create.content.kinetics.transmission.sequencer.SequencedGearshiftBlockEntity
import com.simibubi.create.content.kinetics.transmission.sequencer.SequencerInstructions
import kotlin.math.abs

class SequencedGearshiftAdapter(
    private val blockEntity: SequencedGearshiftBlockEntity
) {
    fun rotate(angle: Int, modifier: Int) {
        runInstruction(SequencerInstructions.TURN_ANGLE, angle, modifier)
    }

    fun move(distance: Int, modifier: Int) {
        runInstruction(SequencerInstructions.TURN_DISTANCE, distance, modifier)
    }

    fun isRunning(): Boolean = !blockEntity.isIdle

    private fun runInstruction(type: SequencerInstructions, value: Int, modifier: Int) {
        if (blockEntity.level == null) {
            throw CreatePeripheralException("sequenced gearshift is not attached to a level")
        }

        val instructions = blockEntity.instructions
        instructions.clear()
        instructions.add(
            Instruction(
                type,
                InstructionSpeedModifiers.getByModifier(modifier),
                abs(value)
            )
        )
        instructions.add(Instruction(SequencerInstructions.END))
        blockEntity.run(0)
    }
}
