# Homework 3: Santorini (Part 1)

In this assignment, you will design and implement the core logic of a boardgame called Santorini (without god cards). The focus of this assignment is on considering design alternatives for code. In Homework 5, we will revisit the game and extend it with god cards, with a GUI, and with an AI opponent. 

This assignment is intended as a gentle introduction to modeling on a relatively simple problem, but it requires going through all steps of the design process.

This assignment has the following learning goals:

* Demonstrate a comprehensive design and development process including object-oriented analysis, object-oriented design, and implementation.
* Demonstrate the use of design goals to influence your design choices, assigning responsibilities carefully, using design patterns where appropriate, discussing trade-offs among alternative designs, and choosing an appropriate solution. The core logic of your solution must be testable and completely independent from your solution’s eventual graphical user interface (GUI).
* Communicate design ideas clearly, including design documents that demonstrate fluency with the basic notation of UML class diagrams and interaction diagrams, the correct use of design vocabulary, and an appropriate level of formality in the specification of system behavior.

## Task 1: Object-Oriented Analysis & Design

For this milestone, you will analyze and design the game *Santorini* without god cards (see appendix for rules). You should focus on the game-related functionality of the program, not its user interface. Think of playing the game by calling a sequence of methods, which you could execute in a test case; it is also helpful to think about and possibly sketch out the GUI and how it interacts with the game at this early stage. Note that the game (without god cards) is fairly simple, so you likely won't need more than a few classes/objects/methods.

**Deliverable 1: Domain model.** Create a domain model describing the important concepts of the game. Your domain model should be represented by a UML class diagram; you may optionally include a glossary. For more information on domain models, see Chapter 9 of Larman’s Applying UML and Patterns. Turn this in as `domain-model.pdf` in the root directory of your git repository.

**Deliverable 2: System sequence diagram.** Create a system sequence diagram identifying all interactions between a user and the system when the user plays the game. The system sequence diagram should help you determine what interactions the high-level system makes available to its users. For more information on system sequence diagrams, see Chapter 10 of Larman’s Applying UML and Patterns. Turn this in as `system-sequence-diagram.pdf`.

**Deliverable 3: Behavioral contract.** Provide behavioral contracts for the following interaction initiated by the user: *The user attempts to move a worker*. The contract should explicitly describe the preconditions and postconditions for the interaction, and your behavioral contract should be consistent with your domain model and interaction diagrams. Constructing behavioral contracts should help you envision important changes of internal state of the game when a player interacts with the game. You may provide explicit examples to clarify your contract. For more information on contracts, see Chapter 11 of Larman’s Applying UML and Patterns. Turn this in as `contract.pdf` or `contract.md`.

**Deliverable 4: Object-level interaction diagram.** Create object-level interaction diagrams for the actions (1) *move* and (2) *build*. Your interaction diagram should be a UML interaction diagram (i.e., sequence or communication diagram), optionally accompanied by prose. For more information on interaction diagram notation, see Chapter 15 of Larman’s Applying UML and Patterns. Turn this in as `interaction-move.pdf` and `interaction-build.pdf`.

**Deliverable 5: Object model.** Create an object model of your game, documented as a UML class diagram. The object model should describe the classes and interfaces of your design, as well as their key associations (with cardinalities), attributes, and methods. The objects and methods in your object model should correspond to the objects in your interaction diagrams above and should reflect the interactions defined in your system sequence diagram. For more information on class diagrams, see Chapter 16 of Larman’s Applying UML and Patterns. Model only the core of the game, not GUI elements or test code. Turn this in as `object-model.pdf`.

**Deliverable 6: Justification:** Write a short (≤ 2 pages) description of your object model justifying your design choices. To support your justification, refer to design goals, principles, and patterns where appropriate. To help formulate your justification, consider your design decisions in creating deliverables 2--5. Turn this in as `justification.pdf` or `justification.md`.

The following questions may help you frame the discussion, but we do not expect an answer as part of the justification: How can a player interact with the game? What are the possible actions? How does a game get started? What state does the game need to store? Where should it be stored? How are blocks and domes represented? How does the game determine what is a valid move or a valid placement of a block? How are invalid operations handled? How does the game detect when somebody has won? 

### Peer review option

Design is often an interactive process where you bounce of ideas with others. You may discuss your design with up to *two* other students in the course. Once you have a reasonably complete initial draft, you may show them your diagrams and text (but not your code) and ask each other questions and give each other feedback. You may improve your designs based on their feedback and what you see in those designs that you critique from others, but the final submission must still be yours. 

If you take this option, you need to submit an extra `peer-review.pdf` file with your homework that contains: (1) The names of the student(s) you worked with. (2) The initial version of your designs before you received feedback. (3) A short description of whether and how you changed your design based on feedback you received or other designs you critiqued.

Grading: We will only grade the final submitted deliverables, not your initial designs or how much you helped others or were helped. We will deduct 5 points if you take this option, but `peer-review.pdf` is incomplete. Sharing your work with other students beyond this peer review option (e.g., sharing code or sharing diagrams without providing the `peer-review.pdf` file) will be considered as an academic integrity violation as per our syllabus.

## Task 2: Implementation & Test

Implement the game based on your design and test it. Tests should include unit tests as well as integration tests that set up the game and play sequences of turns. As usual, document your code.

No user interface is needed, neither graphical nor command line interface. It is sufficient that the game can be played through method invocations in tests, where the tests invoke the methods that a GUI would invoke in an interactive game.

We recommend that you complete Task 1 (modeling) before this implementation, but you should update models by insights gained from the implementation. We expect that models and code align.

Deliverables: Commit all your code to your GitHub repository and ensure that your project is built and tested on Travis CI -- which you will need to set up yourself. 

## Submitting your work

As in previous homework assignments you push your solution your Santorini repository on GitHub and submit a link to the final commit to Canvas. A link will look like `https://github.com/CMU-17-214/<reponame>/commit/<commitid>`. 

You can work in the *main* branch or create your own branches. Include the solutions for Task 1 and Task 2 in the same branch. The files for Task 1 should all be located in the root directory of your repository.

## Evaluation

The homework is worth 100 points. We will grade the homework roughly with the following rubric:

**Design (50pt):**

* [ ] 5: The domain model in file `domain-model.pdf` describes the vocabulary of the problem, uses suitable notation, and is at the right level of abstraction
* [ ] 5: The system sequence diagram in file  `system-sequence-diagram.pdf` is reasonably complete, uses suitable notation, and is at the right level of abstraction.
* [ ] 5: The behavior contract in file `contract.pdf` or `contract.md` is reasonably complete regarding pre- and post-conditions.
* [ ] 10: Two object-level interaction diagrams in files `interaction-move.pdf` and `interaction-build.pdf` describe the internal interactions for move and build and are consistent with the object model (i.e., only uses methods and accesses state to which the classes have access according to the model), uses suitable notation, and is at the right level of abstraction.
* [ ] 5: The object model in file  `object-model.pdf` is reasonably complete, uses suitable notation, and is at the right level of abstraction. It is consistent with the system sequence diagram.
* [ ] 20: Design and justification: The design across all models makes reasonable decisions about responsibility assignment. The justification in file `justification.pdf` or `justification.md` uses suitable terminology and discusses design alternatives in a meaningful way, demonstrating an engagement with design principles and tradeoffs. The use or lack of design patterns is appropriate. In particular, we will look for: Where is state stored (players, current player, worker locations, towers, winner)? Who checks whether a move/build is valid? Who performs state updates for move/build? Who checks whether the game is over and who the winner is? Is cohesion reasonable, avoiding classes/objects with too many/few responsibilities? Is unnecessary coupling avoided? Is unnecessary complexity avoided? 
* [ ] 0: If the peer review option is used, an extra document `peer-review.pdf`  is included, covering (1) the other students' names, (2) the old design, (3) a description of changes.

**Implementation (50pt):**

* [ ] 20: All core functionality of the game is implemented and follows all rules as specified. Specifically we will look for: initializing the game, rejecting invalid moves and builds, updating state after moving and building, and determining the winner and ending the game.
* [ ] 5: The public methods of the code are well documented.
* [ ] 10: The game is well tested, using both unit tests and integration tests (i.e., we expect to see tests of individual key functions like validating a move and tests of sequences of game play). The tests follow good practices.
* [ ] 5: The build and tests are automated on Travis CI.
* [ ] 10: The implementation aligns with models. We will look for: same names, state and methods in the same classes/objects, associations cardinalities reflect implementation, and interactions possible as shown in diagrams.

## Appendix 1: Santorini Rules

Santorini has very simple rules, but the game is very extensible. You can find the original rules [online](https://roxley.com/products/santorini). Beyond the actual board game, you can also find an App that implements the game if you want to try to play it.

In a nutshell, the rules are as follows: The game is played on a 5 by 5 grid, where each grid can contain towers consisting of blocks and domes. Two players have two workers each on any field of the grid. Throughout the game, the workers move around and build towers. The first worker to make it on top of a level-3 tower wins.

As setup, both players pick starting positions for both their workers on the grid. Players take turns. In each turn, they select one of their workers, move this worker to an adjacent unoccupied field, and afterward add a block or dome to an unoccupied adjacent field of their new position. Locations with a worker or a dome are considered occupied. Worker can only climb a maximum of one level when moving. Domes can only be built on level-3 towers.

That's it. You probably want to play a few rounds to get a feel for the game mechanics. There are god powers that modify the game behavior, but those will not be relevant until Homework 5.

## Appendix 2: Notation & Tools

To ease communication and avoid ambiguity, we expect all models to use UML notation for class and sequence diagrams. Chapters 9, 10, 15, and 16 of Larman’s Applying UML and Patterns provide many details and guidance on UML notation. We do not require much formality, but we expect that associations are used rather than attributes where appropriate and that each association includes a name and cardinalities. Attributes and methods should be specified correctly, but we do not require precise descriptions of visibility or types. 

It is important that your models demonstrate an understanding of appropriate levels of abstraction. For example, your domain model should not refer to implementation artifacts, and your object model should not include low-level details such as getter and setter methods, unless they aid the general understanding of your design. 

UML contains notation for many advanced concepts, such as loops and conditions in interaction diagrams. You may use UML notation for these advanced concepts, but we do not require you to do so. If you find you need advanced concepts, you may describe such concepts with your own notation or textual comments, as long as you clearly communicate your intent. 

To maximize clarity, we recommend that you draw UML diagrams with software tools. We do not require specific tools, and you may share tool-related tips on Piazza. There are several easy to use online tools like [Draw.io](https://draw.i/) and [Yumly](https://yuml.me/), and also many desktop tools and IDE plugins. We strongly recommend that you do not mechanically extract models from a software implementation; such mechanically generated models are almost always at an inappropriate level of abstraction. We will accept handwritten models or photographs of models (such as whiteboard sketches) if the models are clearly legible.

