package de.charite.compbio.jannovar.reference;

/**
 * Interface for structural variant descriptions.
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public interface StructuralGenomeVariant extends VariantDescription {

	/** @return String with the canonical end point chromosome name */
	public String getChrName2();

	/** @return integer identifying the end point chromosome */
	public int getChr2();

	/** @return zero-based end position of the variant on the end point chromosome */
	public int getPos2();

	/** @return delta of confidence around first position towards left. */
	public int getPosConfidenceIntervalLeft();

	/** @return delta of confidence around first position towards right. */
	public int getPosConfidenceIntervalRight();

	/** @return delta of confidence around second position towards left. */
	public int getPos2ConfidenceIntervalLeft();

	/** @return delta of confidence around second position towards right. */
	public int getPos2ConfidenceIntervalRight();

	/**
	 * @return whether or not the SV is given in symbolic notation (alternative is by its nucleic
	 *         acid representation.
	 */
	public boolean isSymbolic();

	/**
	 * @return whether or not this is a linear variant and can be represented with a
	 *         {@link GenomeInterval} (insertion, deletion, inversion);
	 */
	public boolean isLinear();

}
