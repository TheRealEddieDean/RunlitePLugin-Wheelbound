import pathlib,sys,unittest
from fractions import Fraction
sys.path.insert(0,str(pathlib.Path(__file__).resolve().parents[1]))
from challenge_tier_rules_v2 import scalar_rate,tier_for_probability
from scrape import parse_rarity
class ChallengeTierTests(unittest.TestCase):
    def test_shared_edges_go_to_easier_rank(self):
        for denominator,tier in [(25,'lesser'),(50,'lesser'),(125,'standard'),(750,'greater'),(5000,'superior')]:
            with self.subTest(denominator=denominator):self.assertEqual(tier_for_probability(Fraction(1,denominator)),tier)
    def test_adjacent_outside_boundaries(self):
        for denominator,tier in [(24,None),(51,'standard'),(126,'greater'),(751,'superior'),(5001,None)]:
            with self.subTest(denominator=denominator):self.assertEqual(tier_for_probability(Fraction(1,denominator)),tier)
    def test_source_multiplier_does_not_change_tier(self):
        for expression in ['1/75','2 × 1/75','3 × 1/75']:
            with self.subTest(expression=expression):self.assertEqual(tier_for_probability(scalar_rate(parse_rarity(expression))),'standard')
        self.assertEqual(tier_for_probability(scalar_rate(parse_rarity('3 × 1/1,026'))),'superior')
    def test_nonunit_fraction_is_not_rounded(self):
        self.assertEqual(tier_for_probability(scalar_rate(parse_rarity('499/250,000'))),'greater')
        self.assertEqual(tier_for_probability(Fraction(2,100)),'lesser')
    def test_unresolved_rate_not_promoted(self):
        for expression in ['Rare','~1/75','1/75; 1/150']:
            with self.subTest(expression=expression):self.assertIsNone(scalar_rate(parse_rarity(expression)))
if __name__=='__main__':unittest.main()
