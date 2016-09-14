package de.upb.wdqa.wdvd;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.wdqa.wdvd.features.Feature;
import de.upb.wdqa.wdvd.features.character.*;
import de.upb.wdqa.wdvd.features.character.misc.*;
import de.upb.wdqa.wdvd.features.diff.EnglishLabelTouched;
import de.upb.wdqa.wdvd.features.diff.HasP109Changed;
import de.upb.wdqa.wdvd.features.diff.HasP18Changed;
import de.upb.wdqa.wdvd.features.diff.HasP21Changed;
import de.upb.wdqa.wdvd.features.diff.HasP27Changed;
import de.upb.wdqa.wdvd.features.diff.HasP373Changed;
import de.upb.wdqa.wdvd.features.diff.HasP54Changed;
import de.upb.wdqa.wdvd.features.diff.HasP569Changed;
import de.upb.wdqa.wdvd.features.diff.HasP856Changed;
import de.upb.wdqa.wdvd.features.diff.NumberOfAliasesAdded;
import de.upb.wdqa.wdvd.features.diff.NumberOfAliasesRemoved;
import de.upb.wdqa.wdvd.features.diff.NumberOfBadgesAdded;
import de.upb.wdqa.wdvd.features.diff.NumberOfBadgesRemoved;
import de.upb.wdqa.wdvd.features.diff.NumberOfClaimsAdded;
import de.upb.wdqa.wdvd.features.diff.NumberOfClaimsChanged;
import de.upb.wdqa.wdvd.features.diff.NumberOfClaimsRemoved;
import de.upb.wdqa.wdvd.features.diff.NumberOfDescriptionsAdded;
import de.upb.wdqa.wdvd.features.diff.NumberOfDescriptionsChanged;
import de.upb.wdqa.wdvd.features.diff.NumberOfDescriptionsRemoved;
import de.upb.wdqa.wdvd.features.diff.NumberOfIdentifiersChanged;
import de.upb.wdqa.wdvd.features.diff.NumberOfLabelsAdded;
import de.upb.wdqa.wdvd.features.diff.NumberOfLabelsChanged;
import de.upb.wdqa.wdvd.features.diff.NumberOfLabelsRemoved;
import de.upb.wdqa.wdvd.features.diff.NumberOfQualifiersAdded;
import de.upb.wdqa.wdvd.features.diff.NumberOfQualifiersRemoved;
import de.upb.wdqa.wdvd.features.diff.NumberOfSitelinksAdded;
import de.upb.wdqa.wdvd.features.diff.NumberOfSitelinksChanged;
import de.upb.wdqa.wdvd.features.diff.NumberOfSitelinksRemoved;
import de.upb.wdqa.wdvd.features.diff.NumberOfSourcesAdded;
import de.upb.wdqa.wdvd.features.diff.NumberOfSourcesRemoved;
import de.upb.wdqa.wdvd.features.item.*;
import de.upb.wdqa.wdvd.features.item.misc.*;
import de.upb.wdqa.wdvd.features.label.*;
import de.upb.wdqa.wdvd.features.meta.*;
import de.upb.wdqa.wdvd.features.revision.*;
import de.upb.wdqa.wdvd.features.revision.misc.*;
import de.upb.wdqa.wdvd.features.sentence.*;
import de.upb.wdqa.wdvd.features.statement.*;
import de.upb.wdqa.wdvd.features.user.*;
import de.upb.wdqa.wdvd.features.user.misc.*;
import de.upb.wdqa.wdvd.features.word.*;
import de.upb.wdqa.wdvd.features.word.misc.ProportionOfLanguageAdded;


public class FeatureList {	
	
	final static Logger logger = LoggerFactory.getLogger(FeatureList.class);
	
	private static List<Feature> getFeatureListInternal(boolean allFeatures){
		List<Feature> l = new ArrayList<Feature>();
		
		////////////////////////////////////////////////////////
		// Meta features (used for computing statistics, character n-grams, bag-of-words model, ...)
		////////////////////////////////////////////////////////
		l.add(new RevisionId());
		
		l.add(new UserId());
		l.add(new UserName());		
		l.add(new GroupId());
		
		l.add(new Timestamp());
		l.add(new RevisionLanguage());		
		l.add(new ContentType());		
		l.add(new CommentTail());
		
		l.add(new ItemId());
		l.add(new EnglishItemLabel());
		l.add(new SuperItemId());

		// previously used for computing statistics
		l.add(new LatestInstanceOfItemId());
		l.add(new LatestEnglishItemLabel());
		
		////////////////////////////////////////////////////////
		// Character features
		////////////////////////////////////////////////////////
		l.add(new AlphanumericRatio());
		l.add(new AsciiRatio());
		l.add(new BracketRatio());
		l.add(new DigitRatio());
		l.add(new LatinRatio());
		l.add(new LongestCharacterSequence());			
		l.add(new LowerCaseRatio());						
		l.add(new NonLatinRatio());					
		l.add(new PunctuationRatio());
		l.add(new UpperCaseRatio());
		l.add(new WhitespaceRatio());
		
		if (allFeatures){
			l.add(new ArabicRatio());
			l.add(new BengaliRatio());
			l.add(new CyrillicRatio());
			l.add(new HanRatio());
			l.add(new BrahmiRatio());
			l.add(new MalayalamRatio());
			l.add(new TamilRatio());
			l.add(new TeluguRatio());
		}
		
		////////////////////////////////////////////////////////
		// Word features
		////////////////////////////////////////////////////////
		l.add(new BadWordRatio());
		l.add(new ContainsLanguageWord());
		l.add(new ContainsURL());
		l.add(new LanguageWordRatio());
		l.add(new LongestWord());			
		l.add(new LowerCaseWordRatio());
		l.add(new ProportionOfLinksAdded()); // from ORES baseline
		l.add(new ProportionOfQidAdded()); // from ORES baseline
		l.add(new UpperCaseWordRatio());			
		
		// Used for ORES baseline
		l.add(new ProportionOfLanguageAdded()); // taken from ORES. takes a loooong time
		
		////////////////////////////////////////////////////////
		// Sentence features
		////////////////////////////////////////////////////////
		l.add(new CommentCommentSimilarity());
		l.add(new CommentLabelSimilarity());
		l.add(new CommentTailLength());
		l.add(new CommentSitelinkSimilarity());
		
		if (allFeatures){
			l.add(new CommentTail());			
		}
//		l.add(new WordsFromCommentInText());
//		l.add(new WordsFromCommentInTextWithoutStopWords());
		
		////////////////////////////////////////////////////////
		// Statement features
		////////////////////////////////////////////////////////			
		l.add(new Property());
		l.add(new ItemValue());
		l.add(new LiteralValue());
		
		if (allFeatures){
			l.add(new ItemId());
			l.add(new SuperItemId());			
		}
		
		////////////////////////////////////////////////////////
		// User features
		////////////////////////////////////////////////////////
		l.add(new IsPrivilegedUser());
		l.add(new IsRegisteredUser());
		l.add(new UserCity());
		l.add(new UserContinent());
		l.add(new UserCountry());
		l.add(new UserCounty());			
		l.add(new UserName());
		l.add(new UserRegion());
		l.add(new UserTimeZone());	
		
		// Used by ORES baseline
		l.add(new IsBotUser());
		
		// Misc user features
//		l.add(new AdminUser());
//		l.add(new BotWithoutBotFlagUser());
//		l.add(new ExtensionBotUser());
//		l.add(new GlobalBotUser());
//		l.add(new GlobalRollbackerUser());
//		l.add(new GlobalStewardUser());
//		l.add(new GlobalSysopUser());
//		l.add(new LocalBotUser());
//		l.add(new PropertyCreatorUser());
//		l.add(new RollbackerUser());
//		l.add(new TranslationAdminUser());
			
		
		////////////////////////////////////////////////////////
		// Item features
		////////////////////////////////////////////////////////
		l.add(new HasListLabel());
		l.add(new IsHuman());
		l.add(new ItemId());
		l.add(new LabelCapitalizedWordRatio());
		l.add(new LabelContainsFemaleFirstName());
		l.add(new LabelContainsMaleFirstName());
		
		// Used by ORES
		l.add(new NumberOfLabels()); // taken from ORES baseline
		l.add(new NumberOfDescriptions()); // taken from ORES baseline
		l.add(new NumberOfAliases()); // taken from ORES baseline
		l.add(new NumberOfStatements()); // taken from ORES baseline
		l.add(new NumberOfSitelinks()); // taken from ORES baseline
		l.add(new NumberOfQualifiers()); // taken from ORES baseline
		l.add(new NumberOfReferences()); // taken from ORES baseline
		l.add(new NumberOfBadges()); // taken from ORES baseline		
		l.add(new IsLivingPerson()); // taken from ORES baseline
		
		if (allFeatures){			
			l.add(new SuperItemId());
			l.add(new LatestInstanceOfItemId());
			l.add(new LatestEnglishItemLabel());
		}
		
		////////////////////////////////////////////////////////
		// Revision features
		////////////////////////////////////////////////////////
		l.add(new CommentLength());
		l.add(new IsLatinLanguage());			
		l.add(new PositionWithinSession());
		l.add(new RevisionAction());
		l.add(new RevisionLanguage());
		l.add(new RevisionPrevAction());
		l.add(new RevisionSubaction());			
		l.add(new RevisionTag()); // also used as FILTER baseline
		
		if (allFeatures){
			l.add(new RevisionSize());		
			l.add(new BytesIncrease());			
			l.add(new MinorRevision());
			l.add(new ParentRevisionInCorpus());
			l.add(new Param1());			
			l.add(new Param3());
			l.add(new Param4());								
			l.add(new TimeSinceLastRevision());
		}
		
		////////////////////////////////////////////////////////
		// Diff features
		////////////////////////////////////////////////////////
		l.add(new NumberOfSitelinksAdded());
		l.add(new NumberOfSitelinksRemoved());
		l.add(new NumberOfSitelinksChanged());
		
		l.add(new NumberOfLabelsAdded());
		l.add(new NumberOfLabelsRemoved());
		l.add(new NumberOfLabelsChanged());
		
		l.add(new NumberOfDescriptionsAdded());
		l.add(new NumberOfDescriptionsRemoved());
		l.add(new NumberOfDescriptionsChanged());
		
		l.add(new NumberOfAliasesAdded());
		l.add(new NumberOfAliasesRemoved());
		
		l.add(new NumberOfClaimsAdded());
		l.add(new NumberOfClaimsRemoved());
		l.add(new NumberOfClaimsChanged());		

		
		l.add(new NumberOfIdentifiersChanged());		
		l.add(new EnglishLabelTouched());
		
		l.add(new NumberOfSourcesAdded());
		l.add(new NumberOfSourcesRemoved());
		
		l.add(new NumberOfQualifiersAdded());
		l.add(new NumberOfQualifiersRemoved());
		
		l.add(new NumberOfBadgesAdded());
		l.add(new NumberOfBadgesRemoved());
		
		l.add(new HasP21Changed());
		l.add(new HasP27Changed());
		l.add(new HasP54Changed());
		l.add(new HasP18Changed());
		l.add(new HasP569Changed());
		l.add(new HasP109Changed());
		l.add(new HasP373Changed());
		l.add(new HasP856Changed());
		
		
		////////////////////////////////////////////////////////
		// Labels features
		////////////////////////////////////////////////////////
		l.add(new RollbackReverted());
		
		if(allFeatures){
			l.add(new UndoRestoreReverted());
		}
		
		return l;		
	}	
	
	public static List<Feature> getFeatures(boolean allFeatures){
		List<Feature> featureList = getFeatureListInternal(allFeatures);
		
		featureList = removeDuplicates(featureList);
		
		return featureList;
	}
	
	private static List<Feature> removeDuplicates(List<Feature> list){
		LinkedHashSet<Feature> linkedHashSet = new LinkedHashSet<Feature>();
		
		for (Feature feature: list){
			if (!linkedHashSet.contains(feature)){
				linkedHashSet.add(feature);
			}
			else{
				logger.debug("Removing duplicate feature: " + feature.getName());
			}			
		}
		
		List<Feature> result = new ArrayList<Feature>(linkedHashSet);
		return result;

	}

}
