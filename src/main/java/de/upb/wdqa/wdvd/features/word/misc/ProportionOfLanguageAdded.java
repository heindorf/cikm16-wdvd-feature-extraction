package de.upb.wdqa.wdvd.features.word.misc;

import java.util.regex.Pattern;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureFloatValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;
import de.upb.wdqa.wdvd.features.Utils;

public class ProportionOfLanguageAdded extends FeatureImpl {	
	// taken from ORES baseline
	private static final String regexStr = "(a(frikaa?ns|lbanian?|lemanha|ng(lais|ol)|ra?b(e?|" +
			"[ei]c|ian?|isc?h)|rmenian?|ssamese|azeri|z[eə]rba" +
			"(ijani?|ycan(ca)?|yjan)|нглийский)|b(ahasa( (indonesia|" +
			"jawa|malaysia|melayu))?|angla|as(k|qu)e|[aeo]ng[ao]?li|" +
			"elarusian?|okmål|osanski|ra[sz]il(ian?)?|ritish( " +
			"kannada)?|ulgarian?)|c(ebuano|hina|hinese( simplified)?" +
			"|zech|roat([eo]|ian?)|atal[aà]n?|рпски|antonese)|[cč]" +
			"(esky|e[sš]tina)|d(an(isc?h|sk)|e?uts?ch)|e(esti|ll[hi]" +
			"nika|ng(els|le(ski|za)|lisc?h)|spa(g?[nñ]h?i?ol|nisc?h)" +
			"|speranto|stonian|usk[ae]ra)|f(ilipino|innish|ran[cç]" +
			"(ais|e|ez[ao])|ren[cs]h|arsi|rancese)|g(al(ego|ician)|" +
			"uja?rati|ree(ce|k)|eorgian|erman[ay]?|ilaki)|h(ayeren|" +
			"ebrew|indi|rvatski|ungar(y|ian))|i(celandic|ndian?|" +
			"ndonesian?|ngl[eê]se?|ngilizce|tali(ano?|en(isch)?))|" +
			"ja(pan(ese)?|vanese)|k(a(nn?ada|zakh)|hmer|o(rean?|" + 
			"sova)|urd[iî])|l(at(in[ao]?|vi(an?|e[sš]u))|ietuvi[uų]" +
			"|ithuanian?)|m(a[ck]edon(ian?|ski)|agyar|alay(alam?|" +
			"sian?)?|altese|andarin|arathi|elayu|ontenegro|ongol" +
			"(ian?)|yanmar)|n(e(d|th)erlands?|epali|orw(ay|egian)|" +
			"orsk( bokm[aå]l)?|ynorsk)|o(landese|dia)|p(ashto|" +
			"ersi?an?|ol(n?isc?h|ski)|or?tugu?[eê]se?(( d[eo])? " +
			"brasil(eiro)?| ?\\(brasil\\))?|unjabi)|r(om[aâi]ni?[aă]n?" +
			"|um(ano|änisch)|ussi([ao]n?|sch))|s(anskrit|erbian|" +
			"imple english|inha?la|lov(ak(ian?)?|enš?[cč]ina|" +
			"en(e|ij?an?)|uomi)|erbisch|pagnolo?|panisc?h|rbeska|" +
			"rpski|venska|c?wedisc?h|hqip)|t(a(galog|mil)|elugu|" +
			"hai(land)?|i[eế]ng vi[eệ]t|[uü]rk([cç]e|isc?h|iş|ey))|" +
			"u(rdu|zbek)|v(alencia(no?)?|ietnamese)|welsh|(англиис|" +
			"[kк]алмыкс|[kк]азахс|немец|[pр]усс|[yу]збекс|" +
			"татарс)кий( язык)??|עברית|[kкқ](аза[кқ]ша|ыргызча|" +
			"ирилл)|українськ(а|ою)|б(еларуская|" +
			"ългарски( език)?)|ελλ[ηι]" +
			"νικ(ά|α)|ქართული|हिन्दी|ไทย|[mм]онгол(иа)?|([cс]рп|" +
			"[mм]акедон)ски|العربية|日本語|한국(말|어)|‌हिनद़ि|" +
			"বাংলা|ਪੰਜਾਬੀ|मराठी|ಕನ್ನಡ|اُردُو|தமிழ்|తెలుగు|ગુજરાતી|" +
			"فارسی|پارسی|മലയാളം|پښتو|မြန်မာဘာသာ|中文(简体|繁體)?|" +
			"中文（(简体?|繁體)）|简体|繁體)";
	
	public static final Pattern pattern = Pattern.compile(regexStr);

	@Override
	public FeatureFloatValue calculate(Revision revision) {
		double oldCount = 0.0;		
		Revision prevRevision = revision.getPreviousRevision();
		if (prevRevision != null){
			oldCount = prevRevision.getTextRegex().getNumberOfLanguageWords();
		}
		
		double newCount = revision.getTextRegex().getNumberOfLanguageWords();
		
		float result = Utils.proportion(oldCount, newCount);
		
		return new FeatureFloatValue(result);
	}

}