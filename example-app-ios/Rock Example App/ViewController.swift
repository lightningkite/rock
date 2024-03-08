//
//  ViewController.swift
//  Rock Example App
//
//  Created by Joseph Ivie on 12/14/23.
//

import UIKit
import shared

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        PlatformSpecificScreen_iosKt.makeMeAGradient = {
            let layer = CAGradientLayer()
            layer.colors = [UIColor.red.cgColor, UIColor.blue.cgColor]
            layer.locations = [0, 1]
            layer.frame = CGRect(x: 0, y: 0, width: 100, height: 100)
            return layer
        }
        RootSetupIosKt.setup(self, app: { $0.app() })
    }


}

