//
//  ContentView.swift
//  example-app-ios
//
//  Created by Joseph Ivie on 4/4/23.
//

import SwiftUI
import shared

struct ContentView: View {
    var body: some View {
        VStack {
            Image(systemName: "globe")
                .imageScale(.large)
                .foregroundColor(.accentColor)
            Text(MyAppKt.platform)
        }
        .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

func quickTest(view: UIView) {
    MyAppKt.setViewToRed(view: view)
}
