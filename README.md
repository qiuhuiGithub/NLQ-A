# NLQ-A
基于gStore的自然语言问答系统

一.系统简介

本系统是一个自然语言问答系统，支持形式化查询语言(SPARQL)查询、自然语言查询和关键词查询。由于gStore只支持SPARQL查询，所以我们构建了这个系统，把自然语言转化成SPARQL语言，然后利用gStore进行查询。

二.系统功能

本系统主要有三个功能，SPARQL查询、自然语言查询和关键词查询。我们利用struts2搭建了一个web端用于显示查询结果，通过实现一个java接口，使查询结果能够在web端显示。再进行查询同时，我们需要打开gStore的server端。

2.1SPARQL查询

用户可以输入SPARQL语句进行查询，目前只支持select语句。
例如当我们输入一个SPARQL查询语句：
“select ?x where{?x　<<result>ub:capitalOf><<result>ub:China>　.}”，客户端的结果如下所示：<br>
result   
There has answer: 1   
<<result>Beijing><br>   

2.2自然语言查询

我们首先把自然语言转换成SPARQL，然后利用SPARQL进行查询。我们将输出我们提取出的三元组、三元组中词汇消歧后的结果以及构造出来的SPARQL语句。我们对每一个转换成的SPARQL给定了一个分数，返回分数最高的那几个SPARQL语句。
例如当我们输入一个问句：
“Who was married to an actor that play in Philadelphia?”，客户端的结果如下所示：

关系列表   
主语列表	谓语列表	宾语列表   
Who	 married_to	 actor   
actor	 play_in	 Philadelphia   

支持集列表<br>
主语支持集	Who, actor, histrion, player,<br>
谓语支持集	marryTo, get_marriedTo, wedTo, playIn,<br>
宾语支持集	actor, histrion, player, Philadelphia, City_of_Brotherly_Love,<br>
sparql语句列表<br>
select ?x where{ ?x <<result>ub:marryTo> ?y. ?y <<result>rdf:type> <<result>actor>. ?y <<result>ub:playIn> <<result>Philadelphia>. }     2.0   
select ?x where{ ?x <<result>ub:marryTo> ?y. ?y <<result>rdf:type> <<result>actor>. ?y <<result>ub:playIn> <<result>City_of_Brotherly_Love>. }   1.9664890375          
select ?x where{ ?x <<result>ub:marryTo> ?y. ?y <<result>rdf:type> <<result>histrion>. ?y <<result>ub:playIn> <<result>Philadelphia>. }      1.9391256071     
select ?x where{ ?x <<result>ub:marryTo> ?y. ?y <<result>rdf:type> <<result>histrion>. ?y <<result>ub:playIn> <<result>City_of_Brotherly_Love>. }    1.9363291713      
select ?x where{ ?x <<result>ub:marryTo> ?y. ?y <<result>rdf:type> <<result>player>. ?y <<result>ub:playIn> <<result>Philadelphia>. }    1.9335109624      </br>  
2.3关键词查询>

实现关键词查询是一个交互的过程。我们首先需要选择一个本体作为关系挖掘的本体，根据用户输入的关键词，找出用户最可能希望表达的一些关系，然后用户针对每一个关键词选择对应的关系，根据用户的选择，得到候选实体和关系。然后进行消歧、构建形式化查询语言SPARQL和实现排序，返回分数最高的那几个SPARQL语句。   
例如当我们输入关键词:China,North。首先从选择的本体中查询出每一个单词可能对应的关系。   
    China:capitalOf、establish、majorOf   
    North:liveIn、locationIn、love   
然后用户选择自己希望表达的关系：capitalOf、LocationIn。根据用户的选择，我们构建形式化的查询语言SPARQL，得到的结果如下：   
select ?x where{ ?x \<ub:locationIn\> \<North\>. ?x \<ub:capitalOf\> \<China\>. } 2.0   
select ?x where{ ?x \<ub:locationIn\> \<North\>. ?x \<ub:capitalOf\> \<People's_Republic_of_China\>. }   1.9337241631   
select ?x where{ ?x \<ub:locationIn\> \<North\>. ?x \<ub:capitalOf\> \<mainland_China\>. }   1.8740759099   </br>
