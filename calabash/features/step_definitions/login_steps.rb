Then /^I login with correct credentials$/ do
	enter_text("EditText id:'login_user_name'","test")
	sleep(1)
	enter_text("EditText id:'login_password'","test")

	touch(query("* marked:'password_login_submit'"))

	sleep(1)
end
