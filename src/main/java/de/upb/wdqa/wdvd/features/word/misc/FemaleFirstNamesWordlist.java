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

public class FemaleFirstNamesWordlist extends FeatureImpl {

	// List of Top 300 female English first names
	// http://names.mongabay.com/male_names_alpha.htm
	// last updated on November 4, 2015
	public final static String[] femaleFirstNames = { "AGNES", "ALICE",
			"ALICIA", "ALLISON", "ALMA", "AMANDA", "AMBER", "AMY", "ANA",
			"ANDREA", "ANGELA", "ANITA", "ANN", "ANNA", "ANNE", "ANNETTE",
			"ANNIE", "APRIL", "ARLENE", "ASHLEY", "AUDREY", "BARBARA",
			"BEATRICE", "BECKY", "BERNICE", "BERTHA", "BESSIE", "BETH",
			"BETTY", "BEVERLY", "BILLIE", "BOBBIE", "BONNIE", "BRANDY",
			"BRENDA", "BRITTANY", "CARLA", "CARMEN", "CAROL", "CAROLE",
			"CAROLINE", "CAROLYN", "CARRIE", "CASSANDRA", "CATHERINE", "CATHY",
			"CHARLENE", "CHARLOTTE", "CHERYL", "CHRISTINA", "CHRISTINE",
			"CHRISTY", "CINDY", "CLAIRE", "CLARA", "CLAUDIA", "COLLEEN",
			"CONNIE", "CONSTANCE", "COURTNEY", "CRYSTAL", "CYNTHIA", "DAISY",
			"DANA", "DANIELLE", "DARLENE", "DAWN", "DEANNA", "DEBBIE",
			"DEBORAH", "DEBRA", "DELORES", "DENISE", "DIANA", "DIANE",
			"DIANNE", "DOLORES", "DONNA", "DORA", "DORIS", "DOROTHY", "EDITH",
			"EDNA", "EILEEN", "ELAINE", "ELEANOR", "ELIZABETH", "ELLA",
			"ELLEN", "ELSIE", "EMILY", "EMMA", "ERICA", "ERIKA", "ERIN",
			"ESTHER", "ETHEL", "EVA", "EVELYN", "FELICIA", "FLORENCE",
			"FRANCES", "GAIL", "GEORGIA", "GERALDINE", "GERTRUDE", "GINA",
			"GLADYS", "GLENDA", "GLORIA", "GRACE", "GWENDOLYN", "HAZEL",
			"HEATHER", "HEIDI", "HELEN", "HILDA", "HOLLY", "IDA", "IRENE",
			"IRMA", "JACKIE", "JACQUELINE", "JAMIE", "JANE", "JANET", "JANICE",
			"JEAN", "JEANETTE", "JEANNE", "JENNIE", "JENNIFER", "JENNY",
			"JESSICA", "JESSIE", "JILL", "JO", "JOAN", "JOANN", "JOANNE",
			"JOSEPHINE", "JOY", "JOYCE", "JUANITA", "JUDITH", "JUDY", "JULIA",
			"JULIE", "JUNE", "KAREN", "KATHERINE", "KATHLEEN", "KATHRYN",
			"KATHY", "KATIE", "KATRINA", "KAY", "KELLY", "KIM", "KIMBERLY",
			"KRISTEN", "KRISTIN", "KRISTINA", "LAURA", "LAUREN", "LAURIE",
			"LEAH", "LENA", "LEONA", "LESLIE", "LILLIAN", "LILLIE", "LINDA",
			"LISA", "LOIS", "LORETTA", "LORI", "LORRAINE", "LOUISE", "LUCILLE",
			"LUCY", "LYDIA", "LYNN", "MABEL", "MAE", "MARCIA", "MARGARET",
			"MARGIE", "MARIA", "MARIAN", "MARIE", "MARILYN", "MARION",
			"MARJORIE", "MARLENE", "MARSHA", "MARTHA", "MARY", "MATTIE",
			"MAUREEN", "MAXINE", "MEGAN", "MELANIE", "MELINDA", "MELISSA",
			"MICHELE", "MICHELLE", "MILDRED", "MINNIE", "MIRIAM", "MISTY",
			"MONICA", "MYRTLE", "NANCY", "NAOMI", "NATALIE", "NELLIE",
			"NICOLE", "NINA", "NORA", "NORMA", "OLGA", "PAMELA", "PATRICIA",
			"PATSY", "PAULA", "PAULINE", "PEARL", "PEGGY", "PENNY", "PHYLLIS",
			"PRISCILLA", "RACHEL", "RAMONA", "REBECCA", "REGINA", "RENEE",
			"RHONDA", "RITA", "ROBERTA", "ROBIN", "ROSA", "ROSE", "ROSEMARY",
			"RUBY", "RUTH", "SALLY", "SAMANTHA", "SANDRA", "SARA", "SARAH",
			"SHANNON", "SHARON", "SHEILA", "SHELLY", "SHERRI", "SHERRY",
			"SHIRLEY", "SONIA", "STACEY", "STACY", "STELLA", "STEPHANIE",
			"SUE", "SUSAN", "SUZANNE", "SYLVIA", "TAMARA", "TAMMY", "TANYA",
			"TARA", "TERESA", "TERRI", "TERRY", "THELMA", "THERESA", "TIFFANY",
			"TINA", "TONI", "TONYA", "TRACEY", "TRACY", "VALERIE", "VANESSA",
			"VELMA", "VERA", "VERONICA", "VICKI", "VICKIE", "VICTORIA",
			"VIOLA", "VIOLET", "VIRGINIA", "VIVIAN", "WANDA", "WENDY",
			"WILLIE", "WILMA", "YOLANDA", "YVONNE"};

	private final static Pattern pattern;

	static {
		List<String> tokens = new ArrayList<String>(
				Arrays.asList(femaleFirstNames));

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