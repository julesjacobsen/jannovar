package de.charite.compbio.jannovar.annotation.builders;

import de.charite.compbio.jannovar.reference.StructuralGenomeVariant;
import de.charite.compbio.jannovar.reference.TranscriptModel;

/**
 * Base class for annotation builders for structural variant.
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
abstract class SVAnnotationBuilder {

	/** The {@link StructuralGenomeVariant} to annotate. */
	protected final StructuralGenomeVariant variant;
	/** The transcript to annotate */
	protected final TranscriptModel transcript;
	
	/**
	 * Construct a new {@link SVAnnotationBuilder} object.
	 *
	 * @param variant
	 *            The {@link StructuralGenomeVariant} to build the annotation for.
	 * @param transcript
	 *            The {@link TranscriptModel} to build the annotation for.
	 */
	public SVAnnotationBuilder(StructuralGenomeVariant variant, TranscriptModel transcript) {
		super();
		this.variant = variant;
		this.transcript = transcript;
	}

	/**
	 * @return the variant to annotate
	 */
	public StructuralGenomeVariant getVariant() {
		return variant;
	}

	/**
	 * @return the transcript to annotate
	 */
	public TranscriptModel getTranscript() {
		return transcript;
	}

}
