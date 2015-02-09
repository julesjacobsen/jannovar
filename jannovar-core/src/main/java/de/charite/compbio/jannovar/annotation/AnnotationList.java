package de.charite.compbio.jannovar.annotation;

import java.util.Collection;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMultiset;

import de.charite.compbio.jannovar.Immutable;

/**
 * A list of priority-sorted {@link Annotation} objects.
 *
 * @see AllAnnotationListTextGenerator
 * @see BestAnnotationListTextGenerator
 *
 * @author Manuel Holtgrewe <manuel.holtgrewe@charite.de>
 */
@Immutable
public final class AnnotationList {

	/** empty annotation list */
	public static final AnnotationList EMPTY = new AnnotationList(ImmutableList.<Annotation> of());

	/** the list of the annotations */
	public final ImmutableList<Annotation> entries;

	/**
	 * Construct ImmutableAnnotationList from a {@link Collection} of {@link Annotation} objects.
	 *
	 * @param entries
	 *            {@link Collection} of {@link Annotation} objects
	 */
	public AnnotationList(Collection<Annotation> entries) {
		this.entries = ImmutableList.copyOf(ImmutableSortedMultiset.copyOf(entries));
	}

	/**
	 * @return {@link Annotation} with highest predicted impact, or <code>null</code> if there is none.
	 */
	public Annotation getHighestImpactAnnotation() {
		if (entries.isEmpty())
			return null;
		else
			return entries.get(0);
	}

	/**
	 * Convenience method.
	 *
	 * @return {@link VariantEffect} with the highest impact of all in {@link #entries} or null if {@link #entries} is
	 *         empty or has no annotated effects.
	 */
	public VariantEffect getHighestImpactEffect() {
		final Annotation anno = getHighestImpactAnnotation();
		if (anno.effects.isEmpty())
			return null;
		else
			return anno.effects.first();
	}

	@Override
	public String toString() {
		return "[" + entries + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entries == null) ? 0 : entries.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnnotationList other = (AnnotationList) obj;
		if (entries == null) {
			if (other.entries != null)
				return false;
		} else if (!entries.equals(other.entries))
			return false;
		return true;
	}

}
