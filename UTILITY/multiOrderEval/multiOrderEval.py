#Implementation of aggregating multiple judgements for evaluating ordred list
#See README more more usage details
#
#(c) 2011 by Hyun Duk Kim. All rights reserved.
#Written by Hyun Duk Kim (http://gildong2.com)
#
#Please refer to the following paper for more theoretical details.
#   Hyun Duk Kim, ChengXiang Zhai and Jiawei Han, Aggregation of Multiple Judgments for Evaluating Ordered Lists, Proceedings of the 32nd European Conference on Information Retrieval (ECIR'10), Milton Keynes, UK. 
#


import sys
import os
import random

#Parameters
notAllowGapOption = 0
wOnLength = 1.0
wOnSupport = 1.0
minSup = 0.75
minPatternLength = 2
minPatternLengthRatio = 0.0
maxPatternLengthRatio = 1.0

noiseRatio = 0.0




def convertToDic(a):
	returnDic = {}
	for i in range(len(a)):
		returnDic[a[i]] = i
	return returnDic


#Calculate order score between array a and b
def calOrder(a, b, option):
	N = float(len(a))
	if N != float(len(b)):
		print 'Error: length of array does not match'
	returnVal = 0.0

	aDic = convertToDic(a)
	bDic = convertToDic(b)
	
	if option == 'Kendall':
		D = 0.0  #number of reverse
		for i in range(len(a)-1):
			for j in range(i+1, len(a)):
				if bDic[a[i]] >= bDic[a[j]]:
					D = D + 1.0			
		returnVal = 1.0 - 2.0 * D / (N * (N-1.0) / 2.0)
	elif option == 'Spearman':
		D = 0.0
		for i in a:
			D = D + (aDic[i] - bDic[i]) * (aDic[i] - bDic[i]) 
		returnVal = 1.0 - 6.0 / ( N * (N+1.0) * (N-1.0)) * D	
	else:
		print 'Error: not defined option'	
	#elif option == 'AvgCont':
	#	returnVal = 0.0
		

	returnVal = 0 + (returnVal - (-1))/2

	return returnVal		
		


def calSeqOrder(targetArr, seqPattern, option):
	#option = 0: allow gaps
	returnScore = 0.0
	maxScore = 0.0

	if minPatternLengthRatio == 0.0:
		minPatternLength = 2
	else:
		minPatternLength = int(float(len(targetArr))*minPatternLengthRatio)


	curLength = 1
	for i in seqPattern:
		curLength = curLength + 1
		if curLength >= minPatternLength:
			curLengthSeqPattern = i.keys()
			for j in curLengthSeqPattern:
				curScore = (1 + wOnLength * (curLength-1)) * (1 + wOnSupport * (i[j]-1))
				maxScore = maxScore + curScore
				if existSeqPattern(targetArr, j, option):
					returnScore = returnScore + curScore
	if float(maxScore) == 0.0:
		return 0.0
	else:
		return float(returnScore) / float(maxScore)
				

def existSeqPattern(targetArr, pattern, option):
	if option == 0:
		patternCheckIndex = 0
		for j in range(0, len(targetArr)):
			if targetArr[j] == pattern[patternCheckIndex]:
				patternCheckIndex = patternCheckIndex + 1
				if patternCheckIndex == len(pattern):
					return 1
	elif option == 1:
		patternCheckIndex = 0
		startFlag = 0
		for j in range(0, len(targetArr)):
			if targetArr[j] == pattern[patternCheckIndex]:
				patternCheckIndex = patternCheckIndex + 1
				if patternCheckIndex == len(pattern):
					return 1
			elif startFlag == 0:
				startFlag = 1
			else:
				return 0

	return 0

	
def readOneEvalDataFromFile(inputFileName):
	inputFile = open(inputFileName, 'r')
	returnArr = []
	for line in inputFile:
		curLine = (line.strip())
		returnArr.append((curLine.split(' '))[1:])
	inputFile.close()
	return returnArr


def findSeqPattern(oneFileArr, option):
	#option == 0: allow gaps for patterns
	#	   1: not allow gaps
	patterns = []
	patternLength = 1
	if maxPatternLengthRatio == 0.0:
		maxPatternLength = 2
	else:
		maxPatternLength = int(float(len(oneFileArr[0]))*maxPatternLengthRatio)

	maxSupport = len(oneFileArr)	

	allPatternItem = []
	for i in oneFileArr[0]:
		allPatternItem.append(i)

	curPatternList = allPatternItem
	curProjectedDb = oneFileArr

	#maxPatternLength = 2
	for k in range(2, maxPatternLength+1):
		curPatternDic = {}
		for i in curPatternList:
			nextProjectedDb = findProjectedDb(curProjectedDb, i, option)
			if len(nextProjectedDb) != 0:
				for j in allPatternItem:
					if j not in i:
						nextPattern = i + j	
						nextPatternSupport = findSupport(nextProjectedDb, j, option)
						#if nextPatternSupport != 0:
						if float(nextPatternSupport) >= float(maxSupport)*minSup:
							curPatternDic[nextPattern] = nextPatternSupport
		curPatternList = curPatternDic.keys()		
		patterns.append(curPatternDic)
	#print patterns
	return patterns


def findSupport(curProjectedDb, nextItem, option):
	#return modifed projected with j
	returnProjectedDb = []
	returnSupport = 0
	for i in curProjectedDb:
		if option == 0:
			for j in range(0, len(i)):
				if i[j] == nextItem:
					returnSupport = returnSupport + 1
		elif option == 1:
			if i[0] == nextItem:
				returnSupport = returnSupport + 1
			

	return returnSupport



def findProjectedDb(curProjectedDb, pattern, option):
	#return modifed projected with j
	returnProjectedDb = []
	for i in curProjectedDb:
		if option == 0:
			patternCheckIndex = 0
			for j in range(0, len(i)):
				if i[j] == pattern[patternCheckIndex]:
					patternCheckIndex = patternCheckIndex + 1
					if patternCheckIndex == len(pattern):
						projectedArr = i[j+1:]
						if len(projectedArr) != 0:
							returnProjectedDb.append(projectedArr)	
						break
		if option == 1:
			patternCheckIndex = 0
			startFlag = 0
			for j in range(0, len(i)):
				if i[j] == pattern[patternCheckIndex]:
					patternCheckIndex = patternCheckIndex + 1
					if patternCheckIndex == len(pattern):
						projectedArr = i[j+1:]
						if len(projectedArr) != 0:
							returnProjectedDb.append(projectedArr)	
						break
				elif startFlag == 0:
					startFlag = 1			
				else:
					break

	return returnProjectedDb


def ranGen(inputArr):
	a = []
	for i in inputArr:
		a.append(i)

	start = 0
	end = len(a)
	for i in range(len(a)):
		selected = random.randrange(start, end, 1)
		temp = a[i]
		a[i] = a[selected]
		a[selected] = temp
	return a	


def makeReverse(inputList):
	returnList = []
	for i in inputList:
		returnList.append(i)
	returnList.reverse()
	return returnList

def makeTestArr(allTestArr, inputIndex):
	returnList = []
	for i in range(len(allTestArr)):
		if i!= inputIndex:
			returnList.append(allTestArr[i])
	return returnList

def findCombinedRanking(testArr):
	rankingScore = {}
	for j in testArr:
		for k in range(len(j)):
			if j[k] not in rankingScore.keys():
				rankingScore[j[k]] = 0.0
			rankingScore[j[k]] = rankingScore[j[k]] + float(k)
	returnList = []
	curBestValue = 100000000.0
	curBestItem = ""
	while(len(rankingScore.keys()) > 0):
		for i in rankingScore.keys():
			if rankingScore[i] < curBestValue:
				curBestValue = rankingScore[i]
				curBestItem = i
		returnList.append(curBestItem)
		rankingScore.pop(curBestItem)
		curBestValue = 10000000000.0
		curBestItem = ""
	return returnList




##################################

#  Old implementation ##################################

if not (len(sys.argv)== 3 or len(sys.argv)==4):
	print "python multiOrderEval.py target_order_file_to_evaluate multiple_standard_order_file [option]"
	print "example: python multiOrderEval.py example/goodSystemOrder.txt example/humanOrders.txt 0"
	print "example: python multiOrderEval.py example/badSystemOrder.txt example/humanOrders.txt 0"
	print "        option = 0: FreSPA (default) Frequent Sequential Pattern-based Aggregation"
	print "                        Known to be the best performing among methods proposed in the paper"
	print "                        Parameters for FreSPA is preset based on the experiments."
	print "                        You can change 'parameter' section at the top of the code."
	print "                1: WCA Weighted Correlation Aggregation with Kendall's tau"
	print "                2: WCA Weighted Correlation Aggregation with Spearman's rank correlation"
	print "                3: RBA Rank-based Aggregation with Kendall's tau with Kendall's tau"
	print "                4: RBA Rank-based Aggregation with Kendall's tau with Spearman's rank correlation"
	sys.exit()


inputFileName1 = sys.argv[1]
inputFileName2 = sys.argv[2]
measureType = 0
if len(sys.argv)==4 :
	measureType = int(sys.argv[3])
	

measureType2 = "NA"
if measureType == 1 or measureType == 3:
	measureType2 = "Kendall"
elif measureType == 2 or measureType == 4:
	measureType2 = "Spearman"



testOrder = readOneEvalDataFromFile(inputFileName1)[0]
idealOrder = readOneEvalDataFromFile(inputFileName2)


score = 0.0
if measureType == 0:
	seqPattern = findSeqPattern(idealOrder, notAllowGapOption)	
	score = calSeqOrder(testOrder, seqPattern, notAllowGapOption)
elif measureType == 1 or measureType == 2 :
	score = 0.0
	totalWeight = 0.0
	for j in range(len(idealOrder)):
		curWeight = 0.0

		for m in range(len(idealOrder)):
			if j != m:
				curWeight += calOrder(idealOrder[m], idealOrder[j], measureType2)
		curWeight /= float(len(idealOrder)-1)
		totalWeight = totalWeight + curWeight
		score = score + curWeight * calOrder(testOrder, idealOrder[j], measureType2)
	score = score / totalWeight
elif measureType == 3 or measureType == 4:
	combinedRanking = findCombinedRanking(idealOrder)
	score = calOrder(testOrder, combinedRanking, measureType2)


print score

