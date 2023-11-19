# ShowMoreAndLessTextView
This is a simple Library by which you can implement the functionality for displaying expandable/collapsible text in a TextView. It allows the user to show more or less text content by clicking on a "...more" or "...less" link within the text. The goal is to provide a user-friendly way to manage long text content in a limited space.

> Step 1. Add the JitPack repository to your build file
>
```gradle
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
```

> Step 2. Add the dependency

```gradle
	dependencies {
	        implementation 'com.github.UndefinedParticle:ShowMoreAndLessTextView:1.0.1'
	}
```
