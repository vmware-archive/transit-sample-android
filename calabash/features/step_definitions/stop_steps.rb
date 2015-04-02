Then /^I add an alarm$/ do
	touch(query("* marked:'menu_add'"))
	wait_for_elements_exist("android.widget.TextView {text CONTAINS 'Click to select a stop'}")
	touch(query("* marked:'notification_add_stop'"))

	tap_when_element_exists("android.widget.TextView {text CONTAINS 'Bay'}")
	tap_when_element_exists("android.widget.TextView {text CONTAINS 'Dupont'}")

	touch(query("* marked:'menu_done'"))
end


Then /^I remove an alarm$/ do
	long_press("android.widget.TextView {text CONTAINS 'Bay'}")

	tap_when_element_exists("android.widget.TextView {text CONTAINS 'Remove Notification'}")	
end

Then /^I clear all existing notifications$/ do
	# notifications = query("* marked:'notification_stop'")
	while (query("* marked:'notification_stop'").length > 0)
	# for i in notifications.length .. 0 
		long_press(query("* marked:'notification_stop'"))
		sleep(1)
		tap_when_element_exists("android.widget.TextView {text CONTAINS 'Remove Notification'}")	
		sleep(2)
	end

end