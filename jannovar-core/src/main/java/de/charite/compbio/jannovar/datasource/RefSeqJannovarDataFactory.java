package de.charite.compbio.jannovar.datasource;

import com.google.common.collect.ImmutableList;
import de.charite.compbio.jannovar.data.ReferenceDictionary;
import de.charite.compbio.jannovar.impl.parse.TranscriptParseException;
import de.charite.compbio.jannovar.impl.parse.refseq.RefSeqParser;
import de.charite.compbio.jannovar.reference.TranscriptModel;
import java.util.List;
import org.ini4j.Profile.Section;

// TODO(holtgrem): Report longest transcript as primary one for RefSeq.

/**
 * Creation of {@link JannovarData} objects from a {@link RefSeqDataSource}.
 *
 * @author <a href="mailto:manuel.holtgrewe@charite.de">Manuel Holtgrewe</a>
 */
final class RefSeqJannovarDataFactory extends JannovarDataFactory {

  /**
   * Construct the factory with the given {@link RefSeqDataSource}.
   *
   * @param options configuration for proxy settings
   * @param dataSource the data source to use.
   * @param iniSection {@link Section} with configuration from INI file
   */
  public RefSeqJannovarDataFactory(
      DatasourceOptions options, RefSeqDataSource dataSource, Section iniSection) {
    super(options, dataSource, iniSection);
  }

  @Override
  protected ImmutableList<TranscriptModel> parseTranscripts(
      ReferenceDictionary refDict, String targetDir, List<String> geneIdentifiers)
      throws TranscriptParseException {
    return new RefSeqParser(refDict, targetDir, geneIdentifiers, iniSection).run();
  }
}
