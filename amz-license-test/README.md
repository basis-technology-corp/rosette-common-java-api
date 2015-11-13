# Amazon License Checking Test #

This project builds a command-line application that can be run on an Amazon instance to see if the the Instance Identity Document checking works.

If the 'check' command in the appassembler runs without complaint, licensing is happy.

If you want to test that it resists some sort of spoofing, you would expect it to fail.

To do this, you'd need to use a proxy to intercept the AMZ metadata HTTP request and provide a response that should cause a failure. We could do those tests on
our own infrastructure.
