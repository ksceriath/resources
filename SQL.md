### Explain Plan

#Draft 

[[Postgres]]
--> `Explain (Analyze)` can reveals the explain plan of a query
--> `Seq Scan`  means the table will be scanned sequentially
--> `Index Scan` means that an index will be used to scan the table
--> `SET enable_seqscan TO off;` will force the seq scan to NOT be done, if at all possible.
--> If the result set of a query can contain a high percentage of the total rows, a SeqScan may be preferable to an IndexScan
	--> IndexScan has an added cost of reading the index, and then going back to read the table
--> Indexes are in already sorted order
	--> If a query has an `Order By` clause, and an IndexScan is used, an additional Sort operation may not be required
--> Sorting (if an index scan is not used, e.g. seq scan) will be done in memory
	--> Explain plan lists thjis as : *Sort Method quicksort Memory: 6081kB*
	--> working memory can be tweaked using `SET work_mem='64kB';`
	--> If enough memory is not available for sorting, sorting can be done externally on disk
		--> Explain plan lists this as : *Sort Method: external merge Disk: 4328kB*
--> A query can use multiple indexes if needed and combine the results
	--> For example : using two Bitmap indexes and combining results using a BitmapAnd operation
--> *Null indexes* - excluding null values from an index makes it more efficient. This can be useful if most queries look at not-null values. 


###### References
1. https://pawelurbanek.com/explain-analyze-indexes



#SQL
