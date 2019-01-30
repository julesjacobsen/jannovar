package de.charite.compbio.jannovar.reference;

import de.charite.compbio.jannovar.annotation.Annotation;

/**
 * Minimal description of a variant as triple (position, ref, alt).
 *
 * The reference and alternative allele string are returned as trimmed, first stripping common suffixes then common
 * prefixes. Note that this is not the same as normalized variants (see the link below) but allows for easier querying
 * in programs.
 *
 * @author <a href="mailto:manuel.holtgrewe@charite.de">Manuel Holtgrewe</a>
 * @see <a href="http://genome.sph.umich.edu/wiki/Variant_Normalization">
 *      http://genome.sph.umich.edu/wiki/Variant_Normalization</a>
 */
public interface VariantDescription {

	/** @return String with the canonical chromosome name */
	public String getChrName();

	/** @return integer identifying the chromosome */
	public int getChr();

	/** @return zero-based position of the variant on the chromosome */
	public int getPos();

	/**
	 * @return String with the reference allele in the variant, without common suffix or prefix to reference allele.
	 */
	public String getRef();

	/**
	 * @return String with the alternative allele in the variant, without common suffix or prefix to reference allele.
	 */
	public String getAlt();

	/**
	 * @return Length of affected reference; {@code -1} if non-linear.
	 */
	public int getRefLength();

	/** @return {@link GenomeInterval} with outer borders of confidence interval. */
	default public GenomeInterval getOuterGenomeInterval() {
		return getGenomeInterval();
	}

	/** @return {@link GenomeInterval} with mid borders of confidence interval. */
	public GenomeInterval getGenomeInterval();

	/** @return {@link GenomeInterval} with inner borders of confidence interval. */
	default public GenomeInterval getInnerGenomeInterval() {
		return getGenomeInterval();
	}

	/** @return Return the same variant description but on the other strand. */
	public VariantDescription withStrand(Strand strand);

	/**
	 * @return Length of alternative allele; {@code -1} if non-linear.
	 */
	public int getAltLength();

	int compareTo(Annotation other);

}
