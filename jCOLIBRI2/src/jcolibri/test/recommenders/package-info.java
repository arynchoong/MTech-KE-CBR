/**
Recommenders examples/testing package. 
A detailed explanation of the recommenders extension can be found {@link jcolibri.extensions.recommendation} package documentation.

<table border="2">
<tr>
  <th>Recommender</th>
  <th>Description</th>
  <th>Template</th>
</tr>

<tr>
  <td><a href="rec1/Houses1.html">Recommender1</a></td>
  <td>Simple Single-Shot product recommender using form-filling and KNN retrieval.<p>House case base, flat Attribute-Value case representation. In this system, the user defines the query, i.e., his/her requirements, using a form. The recommender retrieves the k most similar cases using Nearest Neighbour retrieval. The k most similar cases are displayed to the user in a table; and the system finishes without any other interaction.
  <td><a href="Template1_Cycle.jpg">Template1</a></td>
</tr>

<tr>
  <td><a href="rec2/Houses2.html">Recommender2</a></td>
  <td>
   Conversational (type A) recommender using form-filling, Nearest Neighbour retrieval and top k selection. <p>House case base, flat Attribute-Value case representation. <p>This recommender obtains the user preferences using a form. Then it computes  Nearest Neighbour scoring to obtain the most similar cases. If the user does not find the desired item, he/she can refine the requirements using again a form. The form contains initial values and some attributes are hidden (defined by the designer).
  </td>
  <td><a href="Template2_Cycle.jpg">Template2</a></td>
</tr>

<tr>
  <td><a href="rec3/Houses3.html">Recommender3</a></td>
  <td>
  Conversational (type B) recommender using form-filling and Filter-Based retrieval. <p>House case base, flat Attribute-Value case representation. <p> This recommender obtains the user preferences through a form. Then, it performs the  retrieval filtering the items that obbey the user preferences. If the retrieval set is small enough, items are shown to the user. If the retrieval set is too big or  the user does not find the desired item, the system presents again a form to modify the user requirements. The form has some initial values and custom labels.
  </td>
  <td><a href="Template3_Cycle.jpg">Template3</a></td>
</tr>

<tr>
  <td><a href="rec4/Houses4.html">Recommender4</a></td>
  <td>
   Conversational (type B) recommender using Navigation by Asking and Filter retrieval.<p>House case base, flat Attribute-Value case representation. <p>This recommender applies the Navigation by Asking strategy to obtain the user requirements.  This strategy selects an attribute of the items to be asked to the user each iteration. Depending on the values of these attributes a retrieval set is obtained using filtering.  If the retrieval set is small enough it is presented to the user. If it is too big or the user does not find the desired item, the recommender uses again the Navigation by Asking strategy to improve the user requirements.
  </td>
  <td><a href="Template4_Cycle.jpg">Template4</a></td>
</tr>

<tr>
  <td><a href="rec5/Houses5.html">Recommender5</a></td>
  <td>
  Conversational (type A) recommender using Navigation by Asking and KNN retrieval.<p>House case base, flat Attribute-Value case representation.<p>This recommender combines Navigation by Asking and Nearest Neighbour retrieval. To select the attribute asked to the user, it applies the Similarity Influence method. Then, the NN scoring method is executed and the retrieved items are presented to the user. If the user does not find the desired item, the system asks again for the value of another attribute.
  </td>
  <td><a href="Template5_Cycle.jpg">Template5</a></td>
</tr>

<tr>
  <td><a href="rec6/Houses6.html">Recommender6</a></td>
  <td>
  Conversational (type A) recommender using Navigation by Proposing and Filtered+NearestNeighbour+topKselection retrieval. <p>House case base, flat Attribute-Value case representation. <p> This recommender implements the Navigation by Proposing strategy.  This strategy selects and displays some items to the user and the he/she makes a critique over one of the displayed items (i.e.: "like this but cheaper"). Users give their preferences filling a form. The filtering method uses as filters the critiques over the selected product. Finally, the system applies Nearest Neighbour to the filtered set and obtain the most similar items to be displayed to the user.
  </td>
  <td><a href="Template6_Cycle.jpg">Template6</a></td>
</tr>

<tr>
  <td><a href="rec7/Restaurant7.html">Recommender7</a></td>
  <td>
  Single-Shot restaurant recommender based on user profiles, Nearest Neighbour retrieval and top k selection .<p>This example represents a prototypical recommender that obtains the user preferences from a profile,  then computes Nearest Neigbour retrieval + top K selection, displays the retrieved items and finishes.
  </td>
  <td><a href="Template7_Cycle.jpg">Template7</a></td>
</tr>

<tr>
  <td><a href="rec8/Houses8.html">Recommender8</a></td>
  <td>
  Conversational (type B) house recommender using both Navigation by Asking and Navigation by Proposing.<p>This example reproduces the behaviour of the well-known ExpertClerk system. It works as a Navigation by Asking system until the number of cases is small enough and then it changes to Navigation by Proposing.
  </td>
  <td><a href="Template8_Cycle.jpg">Template8</a></td>
</tr>

<tr>
  <td><a href="rec9/Houses9.html">Recommender9</a></td>
  <td>
  Conversational (type A) house recommender using Navigation by Proposing for both One-Off and Interated preference elicitation, and Filtered+NearestNeighbour+selectTopK retrieval. <p>This conversational recommender implements the Navigation by Proposing strategy. Instead of obtaining  the initial query asking to the user, it displays every item in the case base. This way, the user selects one similar to his/her requirements and the system refines the retrieval set for the following iteration. Like other NbP recommenders it uses critiques to filter the working set of items and computes the Nearest Neighbour scoring to obtain the displayed items.
  </td>
  <td><a href="Template9_Cycle.jpg">Template9</a></td>
</tr>

<tr>
  <td><a href="rec10/Houses10.html">Recommender10</a></td>
  <td>
  Conversational (type A) house recommender system using pre-selected cases at one-off preference elicitation, Navigation By Proposing strategy and Filtering+NearestNeighbour+selectTopK retrieval.<p>This conversational recommender implements the Navigation by Proposing strategy. Instead of obtaining the initial query asking to the user, it uses the Expert Clerk Median scoring algorithm to select k diverse cases that are directly presented to the user in the first iteration. Like other NbP  recommenders it uses critiques to filter the working set of items and computes the Nearest Neighbour scoring to obtain the displayed items.
  </td>
  <td><a href="Template10_Cycle.jpg">Template10</a></td>
</tr>

<tr>
  <td><a href="rec11/Houses11.html">Recommender11</a></td>
  <td>
  Conversational (type A) house recommender using pre-selected cases at one-off preference elicitation, Navigation By Proposing and Filtering+NearestNeighbour+selectTopK retrieval.<p>This recommender follows the Navigation by Proposing strategy and shows the behaviour of several methods that solve the <i>Iterated Preference Elecitiation</i> task that defines how to modify the user preferences.
  </td>
  <td><a href="Template10_Cycle.jpg">Template10</a></td>
</tr>

<tr>
  <td><a href="rec12/MoviesRecommender.html">Recommender12</a></td>
  <td>
  Single-Shot movie recommender obtaining description from profile and scoring cases using collaborative recommendation.<p>This recommender uses a collaborative retrieval algorithm. These collaborative algorithms  return items depending on the recommendations of other users. They require an special organization of the case base to be executed. The query is obtained from a serialized profile following the behaviour of many existing on-line movie recommenders.
  </td>
  <td><a href="Template1_Cycle.jpg">Template1</a></td>
</tr>

<tr>
  <td><a href="rec13/Houses13.html">Recommender13</a></td>
  <td>
  Conversational (type B) recommender using Navigation by Proposing and Filtering + Nearest Neighbour + Compromise Driven Selection. House case base. <p>This recommender follows the Navigation by Proposing strategy with two important features: it uses Compromise Driven selection after the NN scoring and it manages a tabu list of prevously displayed items.
  </td>
  <td><a href="Template11_Cycle.jpg">Template11</a></td>
</tr>

<tr>
  <td><a href="rec14/Houses14.html">Recommender14</a></td>
  <td>
  Single-Shot house recommender using form-filling and Filter-Based retrieval.<p>This example represents the prototypical recommender we usually see in web environments. It obtains the user preferences using a form, retrieves a set of items filtering those that exactly match with the query, displays the retrieved items and finishes.
  </td>
  <td><a href="Template12_Cycle.jpg">Template12</a></td>
</tr>
</table>

<p>
Following table summarizes which features of the framework are included in each example of 
recommender systems.
<center>
<table border="1" cellspacing="2" cellpadding="2" layout-flow:vertical-ideographic;>


<tr>
       <th  align="left">Feature</th>
       <th  align="right">Test</th>
       <th>{@link jcolibri.test.recommenders.rec1 #1}</th>
       <th>{@link jcolibri.test.recommenders.rec2 #2}</th>
       <th>{@link jcolibri.test.recommenders.rec3 #3}</th>
       <th>{@link jcolibri.test.recommenders.rec4 #4}</th>
       <th>{@link jcolibri.test.recommenders.rec5 #5}</th>
       <th>{@link jcolibri.test.recommenders.rec6 #6}</th>
       <th>{@link jcolibri.test.recommenders.rec7 #7}</th>
       <th>{@link jcolibri.test.recommenders.rec8 #8}</th>
       <th>{@link jcolibri.test.recommenders.rec9 #9}</th>
       <th>{@link jcolibri.test.recommenders.rec10 #10}</th>
       <th>{@link jcolibri.test.recommenders.rec11 #11}</th>
       <th>{@link jcolibri.test.recommenders.rec12 #12}</th>
       <th>{@link jcolibri.test.recommenders.rec13 #13}</th>
       <th>{@link jcolibri.test.recommenders.rec14 #14}</th>

</tr>
<tr>
       <TD ROWSPAN=3>Template Type</TD>
       <th  align="left">Single Shot recommender</th>
       <td><img src="ok.jpg"></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td></td>
       <td><img src="ok.jpg"></td>
</tr>
<tr>
       <th  align="left">Conversational A</th>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td></td>
       <td></td>
       <td></td>
</tr>
<tr>
       <th  align="left">Conversational B</th>
       <td></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td></td>
</tr>
<tr>
       <TD ROWSPAN=2>Navigation Type</TD>
       <th  align="left">Navigation by Asking</th>
       <td></td>
       <td></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
</tr>
<tr>
       <th  align="left">Navigation by Proposing</th>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td></td>	   
</tr>
<tr>
       <TD ROWSPAN=3>Preference elicitation</TD>
       <th  align="left">Form Filling</th>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td><img src="ok.jpg"></td>
</tr>
<tr>
       <th  align="left">Asking for an attribute</th>
       <td></td>
       <td></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>	   
</tr>
<tr>
       <th  align="left">Profile</th>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td></td>
       <td></td>
</tr>
<tr>
       <TD ROWSPAN=4>Retrieval</TD>
       <th  align="left">Filtering</th>
       <td></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
</tr>
<tr>
       <th  align="left">NN Scoring</th>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td></td>
</tr>
<tr>
       <th  align="left">Collaborative</th>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td></td>
       <td></td>
</tr>
<tr>
       <th  align="left">Cases Selection</th>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td></td>
</tr>
<tr>
       <TD ROWSPAN=2></TD>
       <th  align="left">Tabu List</th>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td></td>
</tr>
<tr>
       <th  align="left">Create Complex Query from<br>Critiques or selected Case</th>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td><img src="ok.jpg"></td>
       <td></td>
       <td><img src="ok.jpg"></td>
       <td></td>
</tr>

<tr>
       <th  align="left">Feature</th>
       <th  align="right">Test</th>
       <th>{@link jcolibri.test.recommenders.rec1 #1}</th>
       <th>{@link jcolibri.test.recommenders.rec2 #2}</th>
       <th>{@link jcolibri.test.recommenders.rec3 #3}</th>
       <th>{@link jcolibri.test.recommenders.rec4 #4}</th>
       <th>{@link jcolibri.test.recommenders.rec5 #5}</th>
       <th>{@link jcolibri.test.recommenders.rec6 #6}</th>
       <th>{@link jcolibri.test.recommenders.rec7 #7}</th>
       <th>{@link jcolibri.test.recommenders.rec8 #8}</th>
       <th>{@link jcolibri.test.recommenders.rec9 #9}</th>
       <th>{@link jcolibri.test.recommenders.rec10 #10}</th>
       <th>{@link jcolibri.test.recommenders.rec11 #11}</th>
       <th>{@link jcolibri.test.recommenders.rec12 #12}</th>
       <th>{@link jcolibri.test.recommenders.rec13 #13}</th>
       <th>{@link jcolibri.test.recommenders.rec14 #14}</th>

</tr>
</table>
</center>


@see jcolibri.extensions.recommendation
*/
package jcolibri.test.recommenders;