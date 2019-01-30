package de.charite.compbio.jannovar.annotation.builders;

import de.charite.compbio.jannovar.annotation.Annotation;
import de.charite.compbio.jannovar.annotation.VariantEffect;
import de.charite.compbio.jannovar.reference.GenomeInterval;
import de.charite.compbio.jannovar.reference.SVDeletion;
import de.charite.compbio.jannovar.reference.TranscriptModel;

/**
 * Create annotations for {@link SVDeletion}s.
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public final class SVDeletionAnnotationBuilder {

	/** The transcript to build the annotation for. */
	private final TranscriptModel transcript;
	/** The variant that is to be annotated. */
	private final SVDeletion variant;

	/**
	 * Construct new builder for annotating {@link SVDeletion}s.
	 * 
	 * @param transcript
	 *            The transcript to build the annotation for.
	 * @param variant
	 *            The {@link SVDeletion} to build the annotation for.
	 */
	public SVDeletionAnnotationBuilder(TranscriptModel transcript, SVDeletion variant) {
		super();
		this.transcript = transcript;
		this.variant = variant;
	}

	public Annotation build() {
		final GenomeInterval outerInterval = variant.getOuterGenomeInterval();
		if (outerInterval.contains(transcript.getTXRegion())) {
			System.err.println(VariantEffect.TRANSCRIPT_ABLATION);
		} else if (transcript.getExonRegions().stream().anyMatch(exon -> outerInterval.contains(exon))) {
			System.err.println(VariantEffect.EXON_LOSS_VARIANT);
		}
		// FRAMESHIFT_VARIANT
		// INFRAME_DELETION
		// DISRUPTIVE_INFRAME_DELETION
		// FIVE_PRIME_UTR_TRUNCATION
		// THREE_PRIME_UTR_TRUNCATION
		// SPLICE_REGION_VARIANT
		// SPLICE_ACCEPTOR_VARIANT
		// SPLICE_DONOR_VARIANT
		
		return null;
	}

}
