## CQRS / EventSourcing
##### Softwerkskammer Leipzig
##### 2016-10-18

--

### Agenda

* CQRS in 10 mins
* EventSourcing in 10 mins
* Q&A
* Workshop intro
* Workshop (yes, **you** code) 1-3
* break 
* Workshop (yes, **you** code) 4-7
* Q&A  

--


### *Kudos* 

<table>
 <tr><td style="text-align: right;vertical-align:middle">
<p>Oliver Wolf</p>
<p>https://innoq.com</p>
</td><td><img src="http://s3-eu-west-1.amazonaws.com/speakers2013/56_speaker.jpg" width=200 height=200 /></td></tr>
<tr><td style="text-align: right;vertical-align:middle">
<p>Greg Young</p>
<p>https://goodenoughsoftware.net/</p>
</td><td><img src="http://ncrafts.io/2014/img/speakers/greg.png" width=200 height=200 /></td></tr>
</table>


--

#### "You can do CQRS without EventSourcing, but
#### you cannot do EventSourcing without CQRS."

---

## CQRS

##### COMMAND QUERY RESPONSIBILITY SEGREGATION

--
![](md/c1.png "")
Traditional Layered Architecture

--

![](md/c2.png "")
CQRS Pattern applied – done.

--

## THAT IS IT!?

--

![](md/c3.png "")

pro: scale independently

--

![](md/c4.png "")

pro: optimized Query-Models (denormalization)

--


![](md/c5.png "")

pro: **thin** read layer

--


![](md/c6.png "")

pro: Eventual Consistency & Read-Replicas


--


### Conclusion

CQRS helps with

* asymmetric load / read replicas
* gain from different QueryModels / Technologies
* helps Time-to-Market
* avoids technology Lock-In
* enables local optimization on Query-Models

---

# EventSourcing 
#### as a Concept


--


"An architectural pattern which warrants that your entities (as per Eric Evans’ definition) do not track their internal state by means of *direct serialization or O/R mapping*, but by means of **reading and committing events** to an event store."


--

### EventSourcing

* Architectural Pattern
* EventStore keeps log of Events (Facts)
* 'current' State is 
 * transient
 * disposable 
 * fully/reliably reconstructible from series of Events

--

![](http://image.slidesharecdn.com/fromcrudtoeventsourcinganinvestiblestockuniverse-slides-150319095920-conversion-gate01/95/from-crud-to-event-sourcing-an-investible-stock-universe-14-638.jpg?cb=1426759432)

--


![](md/es.png "")


--


### Pros

* focus on **state transitions**, rather than data structure
* audit log already included
* reports over the past
* no Information-loss (Socks, Item 137)
* replayable
 * basically Time Machine (travel back and forth)
 * history of System state
 * helps debugging
 * no infamous SQL-migration-scripts, just change aggregation and replay

--

### Cons 

* maybe different angle to modeling
* (little) more complex than a CRUD System
* new Challenges like:
 * aggregation performance
 * evolving events 
 * capacity 

proven Patterns for the new challenges ***do*** exist! 

--

### Myths

* artificial approach to modeling <span class="fragment" data-fragment-index="1">(**not** true)</span>
* requires eventual consistent  <span class="fragment" data-fragment-index="2">(**not** true)</span>
* inherently difficult & complex  <span class="fragment" data-fragment-index="3">(**not** true)</span>
* bad performance <span class="fragment" data-fragment-index="4">(**not** true)</span>


--

### Questions up to here?

---

#### EventSourcing Basics
## Workshop

--

## Disclaimer

the code here is

* **NOT** an EventSourcing framework !
* just for demonstration of concepts
* oversimplified
* Java, but can be done in any language
* very basic DI with Spring, but can be done without
* uses Lombok for brevity (just syntactical sugar)

--

![There are many ways to skin a Cat](md/skin_a_cat.jpg "many ways to skin a Cat")

There are many ways to skin a Cat

--

### This Workshop is about 
## **Discussion**
### not Code

---

#### EventSourcing Basics 
## Intro

--

Hello, we are FooBank !

--

### Our Domain

* Local Bank
* physical Counter
* will expand into online-banking

--

### Infrastructure

![](md/es_overview.png "overview")

--

##### Glossary

| Component 	| Responsibility 	|
|---		|---			|
| ApplicationFacade   	| single Entry Point / internal API (optional)  	|
| Command   	| Request for the System to *do* something	|
| CommandBus  	| find CmdHandler for given Cmd   	|
| CommandHandler | accept or reject Command, emit Effects |
| Effects | List of Messages |
| Message	| Event or Command  	|
| Event   	| a given Fact  	|
| EventStore  	| a log of Events *that have happened*  	|
| EventHandler/View   	| process Events, project useful Model   	|
| Query | a Question to a Model |
| QueryModel  	| queried by the outside world, query-optimized data	|
| ValidationModel  	| answers Queries while validating   	|

---

#### EventSourcing Basics  
#### Session 1
### Aggregating to the
### canonical Domain Model

--

### Canonical Domain Model

	public class Account {
	    private final UUID id;
	    private final String firstName;
	    private final String lastName;
	    private int balance = 0;

	    void credit(int amount) { balance += amount; }
	    void debit(int amount)  { balance -= amount; }
	}

--

| UseCase Deposition |
|- |
| &nbsp;  |
| As a Customer |
| i want to **deposit** cash at the counter |
| in order to credit it to my account. |


--

| UseCase Withdrawal |
|- |
| &nbsp;  |
| As a Customer |
| i want to **withdraw** money from my account at the counter |
| in order to cash it out. |


--

![](md/es-foo-dm.png "overview")

--

### What we learn

* Implement **write** side
 * minimal Commands
 * minimal CommandHandlers
 * minimal Events
* Implement **read** side 
 * minimal EventHandlers (Views)
 * that populate the canonical Domain Model

--

git clone https://github.com/uweschaefer/es-basics.git

--
### Session 1
1. Implement **ApplicationFacade.deposit/withdraw**
1. Create **Command classes** for both UseCases (see CreateAccountCommand)
1. Create **CommandHandlers** for both UseCases (see CreateAccountHandler)
1. Create **Event** classes for every UseCase (see AccountCreatedEvent)
1. Extend **AccountView** to aggregate Accounts 
1. Pass the Tests

--

### What just happened?

* complete Roundtrip 
 * Command -> CommandHandler
 * Event -> EventHandler (View)
 * Query via DomainModel

---

#### EventSourcing Basics  
#### Session 2
## Dedicated QueryModel

--

## Overview

![](md/es-foo-rm.png "overview")

--

## Aggregation

AccountView = EventHandler that aggregates Events to an Account.

--

### Not every Query within one Account

--

| UseCase ValuedCustomerReport |
|- |
| &nbsp;  |
| As a Manager, i want |
| a complete report the lists all *valued customers* |
| in order to free them from handling charges. |

--

| Specification ValuedCustomer |
|- |
| &nbsp;  |
| a *valued Customer* |
| has deposited an amount of **>=1000€ at least twice**. |


--

### How **not** to do that

Iterate Accounts and inspect their Depositions one by one.

--

### What we learn

* Aggregate beyond Entity-Boundaries
* Create **dedicated Read Model**
 * find appropriate DataStructure
 * select Event-Types by @EventConsumer Methods

--


### Session 2

1. git clean -fd && git reset --hard session2
1. implement ValuedCustomerReport
1. pass the Tests


--

### What just happened?

* Dedicated Read / Query Model
 * beyond aggregate boundaries 
 * Query-optimized Datastructure
 * PullViews have to actually **pull** the events from the ES at some point.

---


#### EventSourcing Basics 
#### Session 3 (Bonuslevel)
## Rolling Snapshot QueryModel

--

## Overview

* ValuedCustomerSupport aggregates ALL depositions in the System
* gets slower & slower
* how to tacle that?

--

### Problematic

every time a Report is needed, a new View has to be created.

	facade.deposit(...);
        
	ValuedCustomerReportView report1 = new ValuedCustomerReportView(es);
	assertTrue(report1.isValuedCustomer(...));

	facade.deposit(...);
        
	ValuedCustomerReportView report2 = new ValuedCustomerReportView(es);
	assertTrue(report2.isValuedCustomer(...));

--

## What if?

we could reuse a QueryModel, that is being updated, rather than re-created?

	ValuedCustomerReportView report = new ValuedCustomerReportView(es);

	facade.deposit(...);
	assertTrue(report.isValuedCustomer(...));

	facade.deposit(...);
	assertTrue(report.isValuedCustomer(...));


____

 **discuss** what would be necessary, conceptually?

--

## No, really – **Discuss**

--

### What we just learned?

* Concept of Rolling Snapshot

--

### Session 3

1. git clean -fd && git reset --hard session3 
1. look at *View.last*, *View.apply* and *PullView.pullEvents*
1. change ValueCustomerReport appropriately.
1. pass the Tests

--

### What just happened?

* Rolling Snapshot
 * keeps latest 
* Eventstore provides query of EventStream from **after** a particular event
* And yes, we can have more snapshots than one, if needed


--

### Have a break. 

![Have a KitKat](md/kitkat.jpg)

### Have a KitKat

---

#### EventSourcing Basics 
#### Session 4
## Event Design

--


| UseCase Transfer |
|- |
| &nbsp;  |
| As a user, i want to |
| transfer Money from my account to someone else's |
| in order to pay my rent online. |


--

### Acceptance Criteria

* *AccountUnknownException* if receiver or sender account does not exist
* *UnfundedTransferException* if sender does not have enough money (no debt allowed)

--

### What we learn

* Event granularity matters
* Events need to reveal their intent 
* Use of a ValidationModel 
* Commands can be rejected

--

### Session 4

1. git clean -fd && git reset --hard session4
1. Implement *TransferHandler*
1. pass the Tests


--

### What just happened?

* Granularity: Events belong to ONE Aggregate
 * we need SendTransfer, RecieveTransfer
* Events reveal intent 
 * do not reuse WithdrawnEvent etc, its a different UseCase!

---

#### EventSourcing Basics
#### Session 5 (Bonuslevel)
### Dedicated WriteModel / ValidationModel


--

#### Command validation sometimes needs Context


--


### Your take on Criteria 1?

* *AccountUnknownException* if receiver or sender account does not exist


	private boolean exists(UUID id) { 
		return repo.find(id) != null; 
	}


Using AccountView just to find out, if an Account exists is wasteful.

--

### All we really need to know
### is if the aggregate exists.

--


## Overview

![](md/es-foo-wm.png "overview")

--


### CommandHandler
#### Responsibilities 

* validate command
* accept or reject command based on that validation
* emit Messages on accepting 


--

### Session 5

1. git clean -fd && git reset --hard session5
1. implement KnownAccountsView
1. pass the Tests

--


### What just happened?

* dedicated WriteModel / ValidationModel 
* does not have to be DomainModel, as it does not need behaviour

---


#### EventSourcing Basics 
#### Session 6
## Side-Effects


--

* Some Commands may trigger external behavior. 
* Replaying that would be problematic.

--


| UseCase Notification |
|- |
| &nbsp;  |
| As a user, i want to |
| be notified by email when i recieve a transfer |
| in order to buy champagne asap. |


--

### What we learn

* how/where to model Side-Effect

--

### Session 6

1. git clean -fd && git reset --hard session6 
1. use CreditNotificationService to send mail
1. discuss where/how to do it properly
1. hint: see CommandBus.publish()
1. pass the Tests


--


### What just happened?

* CommandBus has to be reliable
* Commands can be Effects, too
* Side-Effects can be modeled as Commands / CommandHandlers 


---

# Intermission

--

What about Consistency?

--

Did we relax Consistency compared to a normal CRUD/ORM implementation?

--

# NO!

--

But where we could, how can we take advantage?


---

#### EventSourcing Basics 
#### Session 7 (Bonuslevel)
## Push-Views

--

Up to now, all views have been *PullView*s, that call *pullEvents()* to stream events into them.

--

### Pro

* we can define when to update the View's State

--

### Con

* we have to Query the EventStore in order to know, if View's State is stale
 * the more Queries we run, the more catastropic this is
 * bad Latency for Queries
 * high Contention on EventStore

--

| UseCase GoldCustomers |
|- |
| &nbsp;  |
| As an accountant, i want to |
| know all the Gold-Customers |
| in order to be extra nice to them. |

--

| Specification Gold Customer |
|- |
| &nbsp;  |
|someone who recieved a transfer **>=10.000€** |
|at least once|

--

### Acceptance Criteria

* report must be **instant!** (low-latency) 
* report must be a collection of Strings "&lt;LASTNAME&gt;, &lt;FIRSTNAME&gt;"
* order is not important 
* only Transfers count – Depositions **must** not be examined
* report does not need to include GoldCustomer that recieved the status in the last few seconds...

--

... which means **Eventual Consistency** is ok

--

![](md/es-foo-push.png "overview")

--

#### What would be necessary to push events to the view?


--

### What we learn

* Use Push-Model for Views
* pros and cons of push vs pull

--

### Session 7

1. git clean -fd && git reset --hard session7
1.  implement GoldCustomers *extends PushView*
1.  pass the Tests

--


### What just happened?

* Implemented a push-View that is updated by processing Events asynchronously
* Push reduces read latency
* introduces eventual consistency 
* introduces concurrency 
* PushViews mostly unusable as Validation Model (not strictly consistent)

---

One possible solution can be found here

git clean -fd && git reset --hard theend

--

### Links

* O.Wolfs CQRS Slides
 * https://speakerdeck.com/owolf/cqrs-for-great-good-2
* Greg Young's Blog
 * https://goodenoughsoftware.net/
* Axon mature ES Framework
 * http://www.axonframework.org/
* Lagom Modern ES Framework based on Akka
 * https://www.lightbend.com/lagom
* Microsofts CQRS/ES Patterns & Practices
 * https://msdn.microsoft.com/en-us/library/jj554200.aspx


--

## Q & A


