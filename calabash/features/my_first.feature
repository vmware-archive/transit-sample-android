Feature: Login feature

  Scenario: As a valid user I can log into my app
    When I press "Password Flow"
    Then I see "Please enter your Pivotal credentials below."

	Then I login with correct credentials
    Then I see "Transit++"

    Scenario: Clear all existing notifications
    When I clear all existing notifications
    Then I see "Your list of notifications is empty."
   
   Scenario: Add a stop
   	When I add an alarm
   	Then I see "Bay"

   Scenario: Remove a stop
   When I remove an alarm
   Then I see "Your list of notifications is empty."