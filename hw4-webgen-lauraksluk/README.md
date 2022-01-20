*Use this file to document the detected design problems and your fixes. For instructions see the main branch of this repository.*

# Design Fixes

In each subsection, provide a short answer, typically not longer than a few bullet points or sentences, addressing all parts of the question indicated in square brackets. Where appropriate point to specific files/classes/methods and *use the design terminology* from the lecture (referring to specific design goals/principles/heuristics/patterns).

In many parts we ask for exactly two instances of a problem. The code often has more problems, so you may chose which ones you describe. In the implementation you should fix all problems, not just the two described here.

Also, your discussions may overlap in that you may have addressed several design problems with the same change. This is okay if a chance indeed relates to multiple design issues or questions.



## SubSubSubSubSubArticles

The way nested articles are represented in the implementation is problematic. Briefly describe why it is problematic and how you fixed it:

The way nested articles are represented in the starter code implementation is problematic, because it is not feasible to represent
realistic levels of nested articles. It is not maintainable; if we were to introduce new types of articles,
or a 4th, 5th,... level of nested articles, it would result in the code breaking. Even if the code does not break, it would require writing a lot of
duplicate/similar code in multiple classes/methods. The nested-articles implementation is not extensible. It also decreases understandability,
and violates encapsulation principles because the client does not have a way to interact with all the components similarly.



This problem was fixed by applying the Composite Pattern for the implementation of nested articles. The `Article`, `SubArticle`, and 
`SubSubArticle` classes were replaced by an `ArticleComponent` interface, and 2 classes `ArticleComposite` and `ArticleLeaf` that implemented
that interface and the `Comparable<T>` interface. The `ArticleComposite` class represented "containers" of articles that either held further
nested "containers", or "leaf" components that are just a single level of article. The `ArticleLeaf` class represented
the "leaf" components. This resulted in changes throughout the project, since now the client has an interface to interact with
both objects and composites uniformly. Changes were made in the `Project`, `ProjectBuilder`, `Renderer`, and `CLI` classes.



## Responsibility Assignment

Describe two problems with responsibility assignment where fields or methods were in inappropriate places, why this was problematic, and how you fixed it:

1. In class `Project`, the method `getTopics(Object p)` performs the needed computation for each different concrete implementation
of the old Article implementation. The old implementation was a recursive method that would obtain the topics of each nested inner article.
This is problematic because the responsibility of a computation should go to the class with
the most information to compute it. If the work of computing the set of topics remains in this method,
it leads to decreased understandability. It also leads to high coupling, which would result in the code suffering from large rippling
effects when changes are made, since any changes or new components added would require this entire code block to change. In addition,
there are too many dependencies, making the code hard to reuse or adapt to a new context. This was fixed by moving the responsibility of finding all
relevant topics per article, into the concrete class of `ArticleComposiite` or `ArticleLeaf`. In each of these 2 classes,
there is a recursive implementation to obtain all the topics in this current article, or delegate the call to further inner articles, combining the final result
and passing back the result to the `Project` class.


2. In class `CLI`, the method `printSize()` has computation to obtain all the content within each article and its nested articles.
The implementation looped through all articles and all of its subarticles nested within to get each content. This is problematic because it exposes the implementation
details to the client (eg. that only 3 nested articles are supported). It also decreases understandability and extensibility, because there are long method calls
to obtain the `getContent()` method, and if new components are to be added, the entire code block
needs to be changed to reflect supporting this new component. This was fixed by using the `ArticleComponent` interface type, rather than each
concrete class type. In addition, the method delegated the responsibility of obtaining all the content in an article,
to each concrete implementation– `ArticleLeaf` and `ArticleComposite`. Then, each output within the delegated method call
gets composed back to the full result.





## Code Duplication

Describe two instances of duplicate code that could be abstracted and reused using methods, inheritance, delegation, or design patterns and how you fixed it:

1. A lot of code duplication existed in the original starter code due to the nested article implementation.
For example, in the `Renderer` class, the methods `renderArticle(Project p, Article a)`, `renderSubArticle(Project p, SubArticle subA)`,
and `renderSubSubArticle(Project p, SubSubArticle subSubA)` all had very close to identical code. This was fixed by using the Composite
Pattern and hence, using the `ArticleComponent` interface type for the parameter, rather than their specific type. In addition,
it required combining the duplicated (3) versions of `getBreadcrumbs(..)` into a single method, along with the duplicated (3) versions
of `getArticleContent(..)`, `getArticleURL(..)`, `getArticlePath(..)`, by using polymorphism to work with the interface type, and delegation
to the 2 concrete Article classes to obtain the articles' paths.


2. The classes `Image` and `Video` both extend from the abstract class
`Media`, with field `mediaPath`. But, both classes included an additional field, the `size`, and a getter
for the `size` field. To fix the duplicated code, the duplicated attribute and getter method were pulled up
into the superclass `Media`, to utilize inheritance of both attributes and methods from a (abstract) superclass.





## Coupling

Describe two instances of unnecessary coupling, describe why this coupling is bad, and how you fixed it:

1. The original nested article implementation had the `Article` class and the `SubArticle` class coupled with each other;
the `Article` class had a list of `SubArticle`, but at the same time, the `SubArticle` class had a parent reference back to 
the `Article` class. The `SubArticle` and `SubSubArticle` classes were coupled the same way with each other. This is problematic
because there is coupling in both directions. This leads to brittle, rippling effects to any change, and a difficulty to extend.
In addition, it's harder to understand in isolation and harder to utilize code reuse. This was fixed by removing the `SubArticle`
and `SubSubArticle` classes, but even with the Composite design pattern, there are still relations between the "outer" articles
and "inner" ones. Thus, there is only a single direction of coupling, with the composite class having references to its children, but not
the children having parent references.


2. In class `Renderer`, the 3 method calls `renderArticle(Project p, Article a)`, `renderSubArticle(Project p, SubArticle a)`,
`renderSubSubArticle(Project p, SubSubArticle a)` and many others resulted in the `Renderer` class to be heavily coupled to the 3 concrete classes
that implemented articles and their subarticles. This is problematic because high coupling to implementations is very unstable. For instance,
if any changes were to occur, such as adding new components or functionality, it would result in all of these methods to reflect large
rippling effects. In addition, there is a heavy decrease in understandability and code reuse. The code would not be able to be reused if any new 
components are added. This was fixed by changing the parameter type to the method call, to reflect the object's interface type,
rather than its implementation type. Thus, the parameter to `renderArticle(..)` becomes `ArticleComponent` rather than 3 different
implementations with each parameter type to be its concrete type. This results in changing the relevant code blocks
that are used as helper methods, to reflect coding against interfaces rather than implementation. For example, the parameter type for articles, was changed in
`getArticlePath(..)`, `getBreadcrumbs(..)`, and `getArticleContent(..)` to be `ArticleComponent`.

   



## Cohesion

Describe two instances objects, classes, or methods that severely violate cohesion (e.g., god class). Describe what concerns are mixed and how you fixed it:

1. The `Renderer` class has too many responsibilities. The method `getStoryContentFragment(AbstractContent n, String p)` was casing on which 
concrete implementation the parameter `n` represented to compute the correct content.
This results in it becoming a "god class". The design principle of high cohesion
states that each module should have a small set of related responsibilities. Since the class is responsible for rendering
the content in each article, the computation to obtain the content, should not be a part of the `Renderer` class. This results
in a decrease in understandability since large responsibilities make understanding this class in isolation, difficult. It also
makes the code suffer from decreased reuse abilities. In the case of reuse, maybe rendering other objects/content, these methods
won't be applicable. This was fixed by delegating the actual computation of obtaining the content in articles, to each concrete class implementation
of `AbstractContent`. Then, the method in the `Renderer` class can simply call the interface method, and there are lesser responsibilities in the `Renderer` class.




2. The `Renderer` class had (3) methods to obtain the path, and hence the URL,
of articles. The methods `getArticlePath(Article article)`, `getSubArticlePath(SubArticle article)`, and
`getSubSubArticlePath(SubSubArticle article)` were tasked to get the path of the designated article, or its inner articles.
This results in the `Renderer` class having too many responsibilities. Thus, it results in decreased understandability. It also
makes the code suffer from decreased reuse abilities. For example, if there were new components to be rendered, there would need to be
a new method to calculate its path, and make the code structure messy. This was fixed by adding a new method, `getAllPaths(Project project)`,
where for each article in this project, delegates the job of
computing the article path, to each concrete implementation article class. Then, the `ArticleComposite` class computes all the possible paths for itself
and all its inner articles. When the `Renderer` class needs an article path, it simply asks for it.


## Extensibility

Describe two aspects of the implementation that are unnecessarily difficult to extend (i.e., require modifications of existing methods, possibly in multiple places) and how you improved it:


1. `AbstractContent` is not that easy to extend, because of its usages in other classes (eg. `Renderer`).
For example, the method `getStoryContentFragment(AbstractContent node)` inside the `Renderer` class relies on the usage of
`instanceof` to switch between the different concrete objects of type `AbstractContent`. Thus, if we were to extend `AbstractContent`
with other implementations/classes, it would require changing the entire code block inside the method, and possibly others too.
This was fixed by adding the abstract version of that method into `AbstractContent`, and implementing it inside each specific implementation class of `AbstractContent`.


2. The original nested article implementation would be very difficult to extend. The `Article`, `SubArticle`, and `SubSubArticle`
classes did not have all their related methods in its own class. There were many methods in the `Project` and `Renderer` classes
that would have required additional code or many changes, if we were to add more components. For example, `getTopics(..)` in `Project`
relied on switching between the different instances of the parameter type. So, if more components of Articles were needed, it would require changing
the internal implementation. The duplicate (3) versions of code with similar functionalities in `Renderer`, is similar, because if other
components were needed, the code would be difficult to extend; it would require writing new, and still duplicated, code.
This was fixed by using the Composite design pattern to allow for delegation of responsibility to the 2 concrete implementation classes of the component interface,
and polymorphism, to allow the client to treat objects and composites uniformly. This way it is easier/simpler to add new kinds of components. 
New components can simply implement the shared methods, and client code can utilize the interface type.



## Encapsulation

Describe at least to instances where a object/class violates encapsulation principles. Describe why it is problematic and how you fixed it:


1. `FormattedTextDocument` class was a "god class", because it contained
   so many declared inner interfaces and classes. There were too many responsibilities
   within that class. It also decreases understandability since the usage of these inner classes and interfaces
   will become long function calls that also violate encapsulation.
   Additionally, it reduces the chance of code reuse.
   This was fixed by moving all inner declared interfaces and classes, such as `Heading` and `PlainTextFragment`,
   to their own
   file/interface/class.


2. The `Renderer` class had (3) methods to obtain the path
   of articles. The methods `getArticlePath(Article article)`, `getSubArticlePath(SubArticle article)`, and
   `getSubSubArticlePath(SubSubArticle article)` were tasked to get the path of the designated article, or its inner articles.
The implementation details were all exposed in the implementation of these methods. For example,
the fact that `SubArticle` had references to its parent `Article`, was exposed in `getSubArticlePath(..)`. If the implementation
of how the articles are represented and organized, such as changing it to the Composite design pattern, 
then the current code of obtaining article paths, breaks. The main principle in encapsulation
is to hide details about an implementation so that in the case where the implemented code is changed, the client code doesn't break.
This was fixed by allowing the concrete implementation classes, `ArticleLeaf` and `ArticleComposite`, to compute their article
paths, and then pass the result to the `Renderer` class. This way, in the case where article implementation changes,
the rendering code remains intact.




## Instanceof

The use of `instanceof` is often an indication of a design problem. Provide two examples of methods that use `instanceof`, discuss why this use is problematic, and how you fixed it.

1. In the class `Renderer`, the method `getStoryContentFragment(AbstractContent storyNode, String relPath)` used a lot of
`instanceof` to switch between the different implementations of `AbstractContent` to determine
the correct computation to obtain the rendering from the input node.
We want different behaviors for different subtypes, but here although objects are treated as they are of the superclass abstract type, it's not a useful abstraction. 
Also, there is a maintainability concern because if we were to add a new class that extends `AbstractContent`, 
it becomes difficult to prevent the code from breaking. Additionally, understandability and readability suffers 
after adding numerous switches/cases. This was fixed by declaring an abstract method, `ContentFragment getContentFragment(TemplateEngine eng)` in
the abstract class, `AbstractContent`, and then implementing it in each of the 4 concrete classes.


2. In the class `Project`, the method `getTopics(Object p)` uses a lot of `instanceof` calls to switch between the different
implementations of the old Article concrete classes. There were if-statements 
for if the parameter object was an `Article`, `SubArticle`, or `SubSubArticle`, and depending on which, the implementation was
close to identical. This usage of `instanceof` is problematic because it leads to a lot of code
duplication within each switch case. It also makes the code/project hard to extend. If a component were to be added,
it would require a lot of changes throughout the project, such as here. Thus, this leads to high chances of error. This was fixed by
utilizing the Composite design pattern, where the `ArticleComponent` interface was used instead of each concrete class type.
In addition, the actual computation (recursive method) of getting each article's topics was moved to give the computation responsibility to each concrete implementation
class of `ArticleComponent`.


   



## Events (Challenge Problem)

If you want to claim bonus points for implementing events, briefly describe your design  and how you ensure that you can nest events in articles and vice versa:

[design description, one paragraph]



## Visitor Pattern (Challenge Problem)

If you want to claim bonus points for implementing the visitor pattern, name the file(s) where you implemented it:

[files]