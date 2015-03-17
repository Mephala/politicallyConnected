package launcher.main;

import launcher.view.LauncherView;
import launcher.view.LoadingData;

class LauncherViewLoader {
	protected LauncherViewLoader() {

	}

	LaunchViewStartupResult launchView(LoadingData loadingData) {
		LaunchViewStartupResult result = new LaunchViewStartupResult();
		try {
			result.lw = new LauncherView(loadingData);
			result.isSuccess = true;
		} catch (Exception e) {
			result.isSuccess = false;
			result.error = e.getMessage();
		}
		return result;
	}
}
