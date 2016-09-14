package de.upb.wdqa.wdvd.features.word.misc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureBooleanValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class ContainsLanguageWord2 extends FeatureImpl {
	
	/**
	 * Adding language as label/description/alias
	 * http://www.wikidata.org/wiki/Special:AbuseFilter/8 Last updated: December
	 * 13, 2014
	 * (modified version: Does also find languages in the middle of the string)
	 */
	private final static String regex = ".*(a(frikaa?ns|lbanian?|lemanha|ng(lais|ol)|ra?b(e?|[ei]c|ian?|isc?h)|rmenian?|ssamese|azeri|z[e\\u0259]rba(ijani?|ycan(ca)?|yjan)|\\u043d\\u0433\\u043b\\u0438\\u0439\\u0441\\u043a\\u0438\\u0439)|b(ahasa( (indonesia|jawa|malaysia|melayu))?|angla|as(k|qu)e|[aeo]ng[ao]?li|elarusian?|okm\\u00e5l|osanski|ra[sz]il(ian?)?|ritish( kannada)?|ulgarian?)|c(ebuano|hina|hinese( simplified)?|zech|roat([eo]|ian?)|atal[a\\u00e0]n?|\\u0440\\u043f\\u0441\\u043a\\u0438|antonese)|[c\\u010d](esky|e[s\\u0161]tina)\r\n|d(an(isc?h|sk)|e?uts?ch)|e(esti|ll[hi]nika|ng(els|le(ski|za)|lisc?h)|spa(g?[n\\u00f1]h?i?ol|nisc?h)|speranto|stonian|usk[ae]ra)|f(ilipino|innish|ran[c\\u00e7](ais|e|ez[ao])|ren[cs]h|arsi|rancese)|g(al(ego|ician)|uja?rati|ree(ce|k)|eorgian|erman[ay]?|ilaki)|h(ayeren|ebrew|indi|rvatski|ungar(y|ian))|i(celandic|ndian?|ndonesian?|ngl[e\\u00ea]se?|ngilizce|tali(ano?|en(isch)?))|ja(pan(ese)?|vanese)|k(a(nn?ada|zakh)|hmer|o(rean?|sova)|urd[i\\u00ee])|l(at(in[ao]?|vi(an?|e[s\\u0161]u))|ietuvi[u\\u0173]|ithuanian?)|m(a[ck]edon(ian?|ski)|agyar|alay(alam?|sian?)?|altese|andarin|arathi|elayu|ontenegro|ongol(ian?)|yanmar)|n(e(d|th)erlands?|epali|orw(ay|egian)|orsk( bokm[a\\u00e5]l)?|ynorsk)|o(landese|dia)|p(ashto|ersi?an?|ol(n?isc?h|ski)|or?tugu?[e\\u00ea]se?(( d[eo])? brasil(eiro)?| ?\\(brasil\\))?|unjabi)|r(om[a\\u00e2i]ni?[a\\u0103]n?|um(ano|\\u00e4nisch)|ussi([ao]n?|sch))|s(anskrit|erbian|imple english|inha?la|lov(ak(ian?)?|en\\u0161?[c\\u010d]ina|en(e|ij?an?)|uomi)|erbisch|pagnolo?|panisc?h|rbeska|rpski|venska|c?wedisc?h|hqip)|t(a(galog|mil)|elugu|hai(land)?|i[e\\u1ebf]ng vi[e\\u1ec7]t|[u\\u00fc]rk([c\\u00e7]e|isc?h|i\\u015f|ey))|u(rdu|zbek)|v(alencia(no?)?|ietnamese)|welsh|(\\u0430\\u043d\\u0433\\u043b\\u0438\\u0438\\u0441|[k\\u043a]\\u0430\\u043b\\u043c\\u044b\\u043a\\u0441|[k\\u043a]\\u0430\\u0437\\u0430\\u0445\\u0441|\\u043d\\u0435\\u043c\\u0435\\u0446|[p\\u0440]\\u0443\\u0441\\u0441|[y\\u0443]\\u0437\\u0431\\u0435\\u043a\\u0441)\\u043a\\u0438\\u0439( \\u044f\\u0437\\u044b\\u043a)??|\\u05e2\\u05d1\\u05e8\\u05d9\\u05ea|[k\\u043a\\u049b](\\u0430\\u0437\\u0430[\\u043a\\u049b]\\u0448\\u0430|\\u044b\\u0440\\u0433\\u044b\\u0437\\u0447\\u0430|\\u0438\\u0440\\u0438\\u043b\\u043b)|\\u0443\\u043a\\u0440\\u0430\\u0457\\u043d\\u0441\\u044c\\u043a(\\u0430|\\u043e\\u044e)|\\u0431(\\u0435\\u043b\\u0430\\u0440\\u0443\\u0441\\u043a\\u0430\\u044f|\\u044a\\u043b\\u0433\\u0430\\u0440\\u0441\\u043a\\u0438( \\u0435\\u0437\\u0438\\u043a)?)|\\u03b5\\u03bb\\u03bb[\\u03b7\\u03b9]\\u03bd\\u03b9\\u03ba(\\u03ac|\\u03b1)|\\u10e5\\u10d0\\u10e0\\u10d7\\u10e3\\u10da\\u10d8|\\u0939\\u093f\\u0928\\u094d\\u0926\\u0940|\\u0e44\\u0e17\\u0e22|[m\\u043c]\\u043e\\u043d\\u0433\\u043e\\u043b(\\u0438\\u0430)?|([c\\u0441]\\u0440\\u043f|[m\\u043c]\\u0430\\u043a\\u0435\\u0434\\u043e\\u043d)\\u0441\\u043a\\u0438|\\u0627\\u0644\\u0639\\u0631\\u0628\\u064a\\u0629|\\u65e5\\u672c\\u8a9e|\\ud55c\\uad6d(\\ub9d0|\\uc5b4)|\\u200c\\u0939\\u093f\\u0928\\u0926\\u093c\\u093f|\\u09ac\\u09be\\u0982\\u09b2\\u09be|\\u0a2a\\u0a70\\u0a1c\\u0a3e\\u0a2c\\u0a40|\\u092e\\u0930\\u093e\\u0920\\u0940|\\u0c95\\u0ca8\\u0ccd\\u0ca8\\u0ca1|\\u0627\\u064f\\u0631\\u062f\\u064f\\u0648|\\u0ba4\\u0bae\\u0bbf\\u0bb4\\u0bcd|\\u0c24\\u0c46\\u0c32\\u0c41\\u0c17\\u0c41|\\u0a97\\u0ac1\\u0a9c\\u0ab0\\u0abe\\u0aa4\\u0ac0|\\u0641\\u0627\\u0631\\u0633\\u06cc|\\u067e\\u0627\\u0631\\u0633\\u06cc|\\u0d2e\\u0d32\\u0d2f\\u0d3e\\u0d33\\u0d02|\\u067e\\u069a\\u062a\\u0648|\\u1019\\u103c\\u1014\\u103a\\u1019\\u102c\\u1018\\u102c\\u101e\\u102c|\\u4e2d\\u6587(\\u7b80\\u4f53|\\u7e41\\u9ad4)?|\\u4e2d\\u6587\\uff08(\\u7b80\\u4f53?|\\u7e41\\u9ad4)\\uff09|\\u7b80\\u4f53|\\u7e41\\u9ad4).*";

	private final static Pattern pattern;

	static {
		pattern = Pattern.compile(regex);
	}
	
	private final Matcher matcher = pattern.matcher("");
	
	@Override
	public FeatureBooleanValue calculate(Revision revision) {
		String text = revision.getParsedComment().getSuffixComment();

		boolean result = false;
		if (text != null) {
			text = text.trim();
			text = text.toLowerCase();

			result = matcher.reset(text).matches();
		}

		return new FeatureBooleanValue(result);
	}

}
