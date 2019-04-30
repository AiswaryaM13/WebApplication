import decimal
import sys

logFileName= sys.argv[1]
with open(logFileName,'r') as f:
    totsearch=0
    totquery=0
    c=0
    for line in f:
        c=c+1
        list=line.split()
        totsearch=long(totsearch)+long(list[2])
        totquery=long(totquery)+long(list[4])
    print"average time to search servlet is ", decimal.Decimal(totsearch)/decimal.Decimal(c)
    print"average time spent on parts that use JDBC per query is ", decimal.Decimal(totquery)/decimal.Decimal(c)
