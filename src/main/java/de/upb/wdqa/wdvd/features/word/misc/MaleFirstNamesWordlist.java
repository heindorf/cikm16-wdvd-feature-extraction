/*
 * Wikidata Vandalism Detector 2016 (WDVD-2016)
 * 
 * Copyright (c) 2016 Stefan Heindorf, Martin Potthast, Benno Stein, Gregor Engels
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.upb.wdqa.wdvd.features.word.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import de.upb.wdqa.wdvd.Revision;
import de.upb.wdqa.wdvd.features.FeatureBooleanValue;
import de.upb.wdqa.wdvd.features.FeatureImpl;

public class MaleFirstNamesWordlist extends FeatureImpl {

	// List of Top 300 male English first names
	// http://names.mongabay.com/male_names_alpha.htm
	// last updated on November 4, 2015
	public static final String[] maleFirstNames = { "AARON", "ADAM", "ADRIAN",
			"ALAN", "ALBERT", "ALBERTO", "ALEX", "ALEXANDER", "ALFRED",
			"ALFREDO", "ALLAN", "ALLEN", "ALVIN", "ANDRE", "ANDREW", "ANDY",
			"ANGEL", "ANTHONY", "ANTONIO", "ARMANDO", "ARNOLD", "ARTHUR",
			"BARRY", "BEN", "BENJAMIN", "BERNARD", "BILL", "BILLY", "BOB",
			"BOBBY", "BRAD", "BRADLEY", "BRANDON", "BRENT", "BRETT", "BRIAN",
			"BRUCE", "BRYAN", "BYRON", "CALVIN", "CARL", "CARLOS", "CASEY",
			"CECIL", "CHAD", "CHARLES", "CHARLIE", "CHESTER", "CHRIS",
			"CHRISTIAN", "CHRISTOPHER", "CLARENCE", "CLAUDE", "CLAYTON",
			"CLIFFORD", "CLIFTON", "CLINTON", "CLYDE", "CODY", "COREY", "CORY",
			"CRAIG", "CURTIS", "DALE", "DAN", "DANIEL", "DANNY", "DARRELL",
			"DARREN", "DARRYL", "DARYL", "DAVE", "DAVID", "DEAN", "DENNIS",
			"DEREK", "DERRICK", "DON", "DONALD", "DOUGLAS", "DUANE", "DUSTIN",
			"DWAYNE", "DWIGHT", "EARL", "EDDIE", "EDGAR", "EDUARDO", "EDWARD",
			"EDWIN", "ELMER", "ENRIQUE", "ERIC", "ERIK", "ERNEST", "EUGENE",
			"EVERETT", "FELIX", "FERNANDO", "FLOYD", "FRANCIS", "FRANCISCO",
			"FRANK", "FRANKLIN", "FRED", "FREDDIE", "FREDERICK", "GABRIEL",
			"GARY", "GENE", "GEORGE", "GERALD", "GILBERT", "GLEN", "GLENN",
			"GORDON", "GREG", "GREGORY", "GUY", "HAROLD", "HARRY", "HARVEY",
			"HECTOR", "HENRY", "HERBERT", "HERMAN", "HOWARD", "HUGH", "IAN",
			"ISAAC", "IVAN", "JACK", "JACOB", "JAIME", "JAMES", "JAMIE",
			"JARED", "JASON", "JAVIER", "JAY", "JEFF", "JEFFERY", "JEFFREY",
			"JEREMY", "JEROME", "JERRY", "JESSE", "JESSIE", "JESUS", "JIM",
			"JIMMIE", "JIMMY", "JOE", "JOEL", "JOHN", "JOHNNIE", "JOHNNY",
			"JON", "JONATHAN", "JORDAN", "JORGE", "JOSE", "JOSEPH", "JOSHUA",
			"JUAN", "JULIAN", "JULIO", "JUSTIN", "KARL", "KEITH", "KELLY",
			"KEN", "KENNETH", "KENT", "KEVIN", "KIRK", "KURT", "KYLE", "LANCE",
			"LARRY", "LAWRENCE", "LEE", "LEO", "LEON", "LEONARD", "LEROY",
			"LESLIE", "LESTER", "LEWIS", "LLOYD", "LONNIE", "LOUIS", "LUIS",
			"MANUEL", "MARC", "MARCUS", "MARIO", "MARION", "MARK", "MARSHALL",
			"MARTIN", "MARVIN", "MATHEW", "MATTHEW", "MAURICE", "MAX",
			"MELVIN", "MICHAEL", "MICHEAL", "MIGUEL", "MIKE", "MILTON",
			"MITCHELL", "MORRIS", "NATHAN", "NATHANIEL", "NEIL", "NELSON",
			"NICHOLAS", "NORMAN", "OSCAR", "PATRICK", "PAUL", "PEDRO", "PERRY",
			"PETER", "PHILIP", "PHILLIP", "RAFAEL", "RALPH", "RAMON",
			"RANDALL", "RANDY", "RAUL", "RAY", "RAYMOND", "REGINALD", "RENE",
			"RICARDO", "RICHARD", "RICK", "RICKY", "ROBERT", "ROBERTO",
			"RODNEY", "ROGER", "ROLAND", "RON", "RONALD", "RONNIE", "ROSS",
			"ROY", "RUBEN", "RUSSELL", "RYAN", "SALVADOR", "SAM", "SAMUEL",
			"SCOTT", "SEAN", "SERGIO", "SETH", "SHANE", "SHAWN", "SIDNEY",
			"STANLEY", "STEPHEN", "STEVE", "STEVEN", "TED", "TERRANCE",
			"TERRENCE", "TERRY", "THEODORE", "THOMAS", "TIM", "TIMOTHY",
			"TODD", "TOM", "TOMMY", "TONY", "TRACY", "TRAVIS", "TROY", "TYLER",
			"TYRONE", "VERNON", "VICTOR", "VINCENT", "VIRGIL", "WADE",
			"WALLACE", "WALTER", "WARREN", "WAYNE", "WESLEY", "WILLARD",
			"WILLIAM", "WILLIE", "ZACHARY" };

	private static final Pattern pattern;

	static {
		List<String> tokens = new ArrayList<String>(
				Arrays.asList(maleFirstNames));

		String patternString = ".*\\b(" + StringUtils.join(tokens, "|")
				+ ")\\b.*";
		pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE
				| Pattern.UNICODE_CASE | Pattern.DOTALL | Pattern.CANON_EQ);
	}

	private final Matcher matcher = pattern.matcher("");

	@Override
	public FeatureBooleanValue calculate(Revision revision) {
		String text = revision.getParsedComment().getSuffixComment();

		boolean result = false;
		if (text != null) {
			result = matcher.reset(text).matches();
		}

		return new FeatureBooleanValue(result);
	}

}
