Transit++ for Android
=====================================================

Transit++ for Android requires Android API 4.0 and above.

PCF SDK Usage
--------------

[PCF Push 1.0.4](http://docs.pivotal.io/mobile/push/android/)<br />
[PCF Data 1.1.0](http://docs.pivotal.io/mobile/data/android/)<br />
[PCF Auth 1.0.0](http://docs.pivotal.io/mobile/data/android/)<br />


Overview
------------------

The application included in this repository demonstrates some of the features in Pivotal CF Mobile Services.

You can use Transit++ to browse bus routes and stops, as well as setting an alarm to get push notifications for upcoming transit alerts.

When a user selects a bus route, stop number, and alarm time, a tag is generated and added to the list of tag subscriptions on the *PCF Push* server (`TTCApi`). The list of subscriptions is then synchronized with the data server via *PCF Data* (`RemoteAdapter::sync`). The data is synchronized every time the user updates the alarm settings.

When a fetch call is made and there is no authorized user, it will trigger *PCF Auth* to display a login screen (`AuthActivity`).

Classes
------------------

`AuthActivity` is a custom class that inherits from `LoginActivity`, which provides methods from the PCF Auth library for password and auth code grant authentication flows. 

`TTCApi` is a helper class for the Push and Auth SDKs to handle push registrations, tag subscriptions, and logout.  

`RemoteAdapter` uses a `KeyValueObject` with a *Collection* and *Key* to fetch and update key-value pairs from the server.  The `KeyValueObject` is configured with values specified in the `pivotal.properties`, located in the assets folder of the project.

`pivotal.properties` should include the following keys:

*pivotal.push.gcmSenderId<br />
pivotal.push.platformUuid<br />
pivotal.push.platformSecret<br />
pivotal.push.serviceUrl<br />
pivotal.push.geofencesEnabled<br /><br />
pivotal.auth.tokenUrl<br />
pivotal.auth.authorizeUrl<br />
pivotal.auth.redirectUrl<br />
pivotal.auth.clientId<br />
pivotal.auth.clientSecret<br />
pivotal.auth.accountType<br />
pivotal.auth.tokenType<br /><br />
pivotal.data.serviceUrl<br />
pivotal.data.collisionStrategy<br />*
