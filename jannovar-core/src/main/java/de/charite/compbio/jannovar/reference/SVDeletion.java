package de.charite.compbio.jannovar.reference;

import com.google.common.collect.ComparisonChain;
import de.charite.compbio.jannovar.Immutable;
import de.charite.compbio.jannovar.annotation.Annotation;

/**
 * Representation of a structural variant deletion.
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
@Immutable
public final class SVDeletion implements StructuralGenomeVariant {

	/** Start position of the change. */
	private final GenomePosition pos;
	/** Delta for confidence interval around first start point towards left. */
	private final int posConfidenceIntervalLeft;
	/** Delta for confidence interval around first start point towards right. */
	private final int posConfidenceIntervalRight;
	/** End position of the change. */
	private final GenomePosition pos2;
	/** Delta for confidence interval around second start point towards left. */
	private final int pos2ConfidenceIntervalLeft;
	/** Delta for confidence interval around second start point towards right. */
	private final int pos2ConfidenceIntervalRight;
	/** Either nucleic acid reference string or {@code "N"} if symbolic. */
	private final String ref;
	/** Either nucleic acid reference string or {@code "<DEL>"} if symbolic. */
	private final String alt;

	/**
	 * Construct new SV deletion.
	 *
	 * @param pos
	 *            genomic start position of variant
	 * @param posConfidenceIntervalLeft
	 *            delta for confidence around start position towards left
	 * @param posConfidenceIntervalRight
	 *            delta for confidence around start position towards right
	 * @param pos2
	 *            genomic end position of variant
	 * @param pos2ConfidenceIntervalLeft
	 *            delta for confidence around end position towards left
	 * @param pos2ConfidenceIntervalRight
	 *            delta for confidence around end position towards right
	 * @param ref
	 *            Either nucleic acid reference string or {@code "N"} if symbolic.
	 * @param alt
	 *            Either nucleic acid reference string or {@code "<DEL>"} if symbolic.
	 */
	public SVDeletion(GenomePosition pos, int posConfidenceIntervalLeft, int posConfidenceIntervalRight,
			GenomePosition pos2, int pos2ConfidenceIntervalLeft, int pos2ConfidenceIntervalRight, String ref,
			String alt) {
		super();

		if (pos.getStrand() != Strand.FWD)
			throw new IllegalArgumentException("pos must be on forward strand");
		if (pos2.getStrand() != Strand.FWD)
			throw new IllegalArgumentException("pos2 must be on forward strand");
		if (wouldBeSymbolicAllele(alt) && !"N".equals(ref.toUpperCase()))
			throw new IllegalArgumentException("If alternative is symbolic, reference must be \"N\"");

		this.pos = pos;
		this.posConfidenceIntervalLeft = posConfidenceIntervalLeft;
		this.posConfidenceIntervalRight = posConfidenceIntervalRight;
		this.pos2 = pos2;
		this.pos2ConfidenceIntervalLeft = pos2ConfidenceIntervalLeft;
		this.pos2ConfidenceIntervalRight = pos2ConfidenceIntervalRight;
		this.ref = ref;
		this.alt = alt;
	}

	/**
	 * @return {@link GenomeInterval} with outer borders of confidence interval.
	 */
	@Override
	public GenomeInterval getOuterGenomeInterval() {
		final GenomePosition pos = this.pos.shifted(this.posConfidenceIntervalLeft);
		final GenomePosition pos2 = this.pos2.shifted(this.pos2ConfidenceIntervalRight);
		final int length = pos.isLt(pos2) ? pos2.differenceTo(pos) : pos.differenceTo(pos2);
		return new GenomeInterval(pos, length);
	}

	/**
	 * @return {@link GenomeInterval} with mid borders of confidence interval.
	 */
	@Override
	public GenomeInterval getGenomeInterval() {
		final int length = this.pos.isLt(this.pos2) ? this.pos2.differenceTo(this.pos)
				: this.pos.differenceTo(this.pos2);
		return new GenomeInterval(this.pos, length);
	}

	/**
	 * @return {@link GenomeInterval} with inner borders of confidence interval.
	 */
	@Override
	public GenomeInterval getInnerGenomeInterval() {
		final GenomePosition pos = this.pos.shifted(this.posConfidenceIntervalRight);
		final GenomePosition pos2 = this.pos2.shifted(this.pos2ConfidenceIntervalLeft);
		final int length = pos.isLt(pos2) ? pos2.differenceTo(pos) : pos.differenceTo(pos2);
		return new GenomeInterval(pos, length);
	}

	@Override
	public String getChrName() {
		return this.pos.getRefDict().getContigIDToName().get(this.pos.getChr());
	}

	@Override
	public int getChr() {
		return this.pos.getChr();
	}

	@Override
	public int getPos() {
		return this.pos.getPos();
	}

	@Override
	public String getRef() {
		return this.ref;
	}

	@Override
	public String getAlt() {
		return this.alt;
	}

	@Override
	public VariantDescription withStrand(Strand strand) {
		throw new UnsupportedOperationException("Cannot change strand of SVDeletion (yet)");
	}

	/**
	 * @return the number of deleted bases
	 */
	public int getNumDeletedBases() {
		return this.getGenomeInterval().length();
	}
	
	@Override
	public int compareTo(Annotation other) {
		// TODO(holtgrewe): it really only makes sense if Annotation would also have a second
		// point...
		return ComparisonChain.start().compare(pos, other.getPos()).compare(ref, other.getRef())
				.compare(alt, other.getAlt()).result();
	}

	@Override
	public String getChrName2() {
		return this.pos2.getRefDict().getContigIDToName().get(this.pos.getChr());
	}

	@Override
	public int getChr2() {
		return this.pos2.getChr();
	}

	@Override
	public int getPos2() {
		return this.pos2.getPos();
	}

	@Override
	public int getPosConfidenceIntervalLeft() {
		return this.posConfidenceIntervalLeft;
	}

	@Override
	public int getPosConfidenceIntervalRight() {
		return this.posConfidenceIntervalRight;
	}

	@Override
	public int getPos2ConfidenceIntervalLeft() {
		return this.pos2ConfidenceIntervalLeft;
	}

	@Override
	public int getPos2ConfidenceIntervalRight() {
		return this.pos2ConfidenceIntervalRight;
	}

	@Override
	public int getRefLength() {
		return this.getNumDeletedBases();
	}

	@Override
	public int getAltLength() {
		return 0;
	}

	@Override
	public boolean isSymbolic() {
		return wouldBeSymbolicAllele(ref) || wouldBeSymbolicAllele(alt);
	}

	@Override
	public boolean isLinear() {
		return true;
	}

	/**
	 * @return <code>true</code> if the given <code>allele</code> string describes a symbolic allele
	 *         (events not described by replacement of bases, e.g. break-ends or duplications that
	 *         are described in one line).
	 */
	private static boolean wouldBeSymbolicAllele(String allele) {
		if (allele.length() <= 1)
			return false;
		// The three terms are: (1) symbolic, (2) single breakend, (3) mated breakend
		return (allele.charAt(0) == '<' || allele.charAt(allele.length() - 1) == '>')
				|| (allele.charAt(0) == '.' || allele.charAt(allele.length() - 1) == '.')
				|| (allele.contains("[") || allele.contains("]"));
	}

}
