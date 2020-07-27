package com.pyraliron.pyralfishmod.events;

import com.google.common.base.Preconditions;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.event.world.NoteBlockEvent.Instrument;
import net.minecraftforge.event.world.NoteBlockEvent.Note;
import net.minecraftforge.event.world.NoteBlockEvent.Octave;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public class AdvancedNoteBlockEvent extends BlockEvent {

	private int noteId;

    protected AdvancedNoteBlockEvent(World world, BlockPos pos, IBlockState state, int note)
    {
        super(world, pos, state);
        this.noteId = note;
    }

    /**
     * Get the Note the Noteblock is tuned to
     * @return the Note
     */
    public Note getNote()
    {
        return Note.fromId(noteId);
    }

    /**
     * Get the Octave of the note this Noteblock is tuned to
     * @return the Octave
     */
    public Octave getOctave()
    {
        return Octave.fromId(noteId);
    }

    /**
     * get the vanilla note-id, which contains information about both Note and Octave. Most modders should not need this.
     * @return an ID for the note
     */
    public int getVanillaNoteId()
    {
        return noteId;
    }

    /**
     * Set Note and Octave for this event.<br>
     * If octave is Octave.HIGH, note may only be Note.F_SHARP
     * @param note the Note
     * @param octave the Octave
     */
    public void setNote(Note note, Octave octave)
    {
        
        this.noteId = note.ordinal() + octave.ordinal() * 12;
    }
    /**
     * Fired when a Noteblock plays it's note. You can override the note and instrument
     * Canceling this event will stop the note from playing.
     */
    @Cancelable
    public static class Play extends AdvancedNoteBlockEvent
    {
        private Instrument instrument;

        public Play(World world, BlockPos pos, IBlockState state, int note, int instrument)
        {
            super(world, pos, state, note);
            this.setInstrument(Instrument.fromId(instrument));
        }

        public Instrument getInstrument()
        {
            return instrument;
        }

        public void setInstrument(Instrument instrument)
        {
            this.instrument = instrument;
        }
    }
    @Cancelable
    public static class Change extends AdvancedNoteBlockEvent
    {
        private final Note oldNote;
        private final Octave oldOctave;

        public Change(World world, BlockPos pos, IBlockState state, int oldNote, int newNote)
        {
            super(world, pos, state, newNote);
            this.oldNote = Note.fromId(oldNote);
            this.oldOctave = Octave.fromId(oldNote);
        }

        public Note getOldNote()
        {
            return oldNote;
        }

        public Octave getOldOctave()
        {
            return oldOctave;
        }
    }
    public static enum Instrument
    {
        PIANO,
        BASSDRUM,
        SNARE,
        CLICKS,
        BASSGUITAR,
        FLUTE,
        BELL,
        GUITAR,
        CHIME,
        XYLOPHONE;

        // cache to avoid creating a new array every time
        private static final Instrument[] values = values();

        static Instrument fromId(int id)
        {
            return id < 0 || id >= values.length ? PIANO : values[id];
        }
    }

    /**
     * Information about the pitch of a Noteblock note.
     * For altered notes such as G-Sharp / A-Flat the Sharp variant is used here.
     *
     */
    public static enum Note
    {
    	
        C,
        C_SHARP,
        D,
        D_SHARP,
        E,
        F,
        F_SHARP,
        G,
        G_SHARP,
        A,
        A_SHARP,
        B;

        private static final Note[] values = values();

        static Note fromId(int id)
        {
            return values[id % 12];
        }
    }

    /**
     * Describes the Octave of a Note being played by a Noteblock.
     * Together with {@link Note} it fully describes the note.
     *
     */
    public static enum Octave
    {
    	LOWEST,
    	LOWER,
        LOW,
        MID,
        HIGH,
        HIGHER,
        HIGHEST; 

        static Octave fromId(int id)
        {
            return id < 12 ? LOWEST : id < 24 ? LOWER: id < 36 ? LOW : id < 48 ? MID : id < 60 ? HIGH : id < 72 ? HIGHER : HIGHEST;
        }
    }
}
