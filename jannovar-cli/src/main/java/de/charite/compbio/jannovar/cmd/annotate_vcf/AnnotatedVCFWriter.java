package de.charite.compbio.jannovar.cmd.annotate_vcf;

import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.variantcontext.writer.VariantContextWriter;
import htsjdk.variant.vcf.VCFFileReader;
import htsjdk.variant.vcf.VCFHeaderLine;

import java.io.File;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import de.charite.compbio.jannovar.JannovarOptions;
import de.charite.compbio.jannovar.annotation.AnnotationException;
import de.charite.compbio.jannovar.htsjdk.InfoFields;
import de.charite.compbio.jannovar.htsjdk.VariantContextAnnotator;
import de.charite.compbio.jannovar.htsjdk.VariantContextWriterConstructionHelper;
import de.charite.compbio.jannovar.impl.util.PathUtil;
import de.charite.compbio.jannovar.io.Chromosome;
import de.charite.compbio.jannovar.io.ReferenceDictionary;

/**
 * Annotate variant in {@link VariantContext} and write out through HTSJDK (i.e. in VCF/BCF format).
 */
public class AnnotatedVCFWriter extends AnnotatedVariantWriter {

	/** {@link ReferenceDictionary} object to use for information about the genome. */
	private final ReferenceDictionary refDict;

	/** path to VCF file to process */
	private final String vcfPath;

	/** configuration to use */
	private final JannovarOptions options;

	/** the {@link VariantContextAnnotator} to use. */
	private final VariantContextAnnotator annotator;

	/** writer for annotated VariantContext objects */
	private final VariantContextWriter out;

	/** command line arguments to Jannovar */
	private final ImmutableList<String> args;

	public AnnotatedVCFWriter(ReferenceDictionary refDict, VCFFileReader reader,
			ImmutableMap<Integer, Chromosome> chromosomeMap, String vcfPath, JannovarOptions options,
			ImmutableList<String> args) {
		this.refDict = refDict;
		this.annotator = new VariantContextAnnotator(refDict, chromosomeMap, new VariantContextAnnotator.Options(
				InfoFields.build(options.writeVCFAnnotationStandardInfoFields, options.writeJannovarInfoFields),
				!options.showAll, options.escapeAnnField, options.nt3PrimeShifting));
		this.vcfPath = vcfPath;
		this.options = options;
		this.args = args;

		final InfoFields fields = InfoFields.build(options.writeVCFAnnotationStandardInfoFields,
				options.writeJannovarInfoFields);
		ImmutableSet<VCFHeaderLine> additionalLines = ImmutableSet.of(new VCFHeaderLine("jannovarVersion",
				JannovarOptions.JANNOVAR_VERSION), new VCFHeaderLine("jannovarCommand", Joiner.on(' ').join(args)));
		this.out = VariantContextWriterConstructionHelper.openVariantContextWriter(reader.getFileHeader(),
				getOutFileName(), fields, additionalLines);
	}

	/** @return output file name, depending on this.options */
	@Override
	public String getOutFileName() {
		File f = new File(vcfPath);
		String outname = f.getName();
		if (options.outVCFFolder != null)
			outname = PathUtil.join(options.outVCFFolder, outname);
		else
			outname = PathUtil.join(f.getParent(), outname);
		int i = outname.toLowerCase().lastIndexOf("vcf");
		if (i < 0)
			return outname + ".jv.vcf";
		else
			return outname.substring(0, i) + "jv.vcf";
	}

	@Override
	public void put(VariantContext vc) throws AnnotationException {
		vc = annotator.applyAnnotations(vc, annotator.buildAnnotationList(vc));
		vc.getCommonInfo().removeAttribute("");
		out.add(vc);
	}

	/** Close VariantContextWriter in out. */
	@Override
	public void close() {
		out.close();
	}

}
