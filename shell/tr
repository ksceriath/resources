>> tr [options] SET1 SET2

Translate the text in standard input, mapping characters in SET1 to SET2.

e.g.
tr a-z A-Z

would translate a to A, b to B, z to Z, likewise..

If SET2 is smaller than SET1, last character of SET2 is repeated as required.
If SET2 is larger than SET1, extra characters in SET2 are ignored.

-d      : delete characters from SET1

e.g.
tr -d a-g

would remove occurrences of characters a to g.

-t      : truncate SET1 to the length of SET2 (instead of repeating the last char as above)