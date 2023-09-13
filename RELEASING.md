# Releasing

1. Update the `DEFAULT_RELEASE_DATE` inside the `com/leinardi/ubuntucountdownwidget/interactor/GetReleaseDateInteractor.kt` file
2. Run the [Update release date](https://github.com/leinardi/UbuntuCountdownWidget/actions/workflows/ubuntu-release-date.yml) Action
3. Crate a fastlane changelog file using the `BuildConfig.VERSION_CODE` as file name with `.txt` as extension.
4. `git commit -am "Updated fastlane changelog"`
5. `git push`
6. Create a PR from [master](../../tree/master) to [release](../../tree/release)
7. Run the [Release](https://github.com/leinardi/UbuntuCountdownWidget/actions/workflows/release.yml) Action
8. Visit [Google Play Console](https://play.google.com/apps/publish/) and promote the release
