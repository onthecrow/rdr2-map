import SwiftUI
import UIKit
import composeApp
import GoogleMaps

struct ContentView: View {
	var body: some View {
		ComposeView()
		    .ignoresSafeArea(/* .keyboard */) // Compose has own keyboard handler
	}
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        GMSServices.provideAPIKey(...) // Put the actual Google API key here
        return MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}